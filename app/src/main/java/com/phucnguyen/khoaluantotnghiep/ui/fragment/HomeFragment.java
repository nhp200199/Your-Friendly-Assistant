package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.viewmodel.UserViewModel;

import java.util.List;

public class HomeFragment extends Fragment {
    private static final String LOGTAG = HomeFragment.class.getSimpleName();
    private RecyclerView productItemContainer;
    private LinearLayout loginHintContainer;
    private FrameLayout productItemsOuterContainer;
    private TextView tvNoProductsFound;
    private TextView tvNetworkError;
    private Button btnGoToLoginScreen;
    private ProgressBar mProgressBar;

    private UserViewModel userViewModel;
    private ProductItemsAdapter adapter;
    private ProductItem mProductItem;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        Log.i("HomeFragment", "onCreate called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connectViews(view);

        NavController navController = NavHostFragment.findNavController(this);
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        final NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.home_fragment);

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        final LifecycleEventObserver observer = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_RESUME)) {
                    if (navBackStackEntry.getSavedStateHandle().contains("wishedPrice") && mProductItem != null) {
                        userViewModel.trackProduct(mProductItem, mProductItem.getDesiredPrice());
                        navBackStackEntry.getSavedStateHandle().remove("wishedPrice");
                    }
                }
            }
        };
        navBackStackEntry.getLifecycle().addObserver(observer);

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                    navBackStackEntry.getLifecycle().removeObserver(observer);
                }
            }
        });

        userViewModel.getTokenIdMLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String tokenId) {
                if (tokenId != null) {
                    productItemsOuterContainer.setVisibility(View.VISIBLE);
                    loginHintContainer.setVisibility(View.GONE);
                } else {
                    productItemsOuterContainer.setVisibility(View.GONE);
                    loginHintContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        userViewModel.getTrackedProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {
                if (productItems == null)
                    return;
                else {
                    adapter.setProductItems(productItems);

                    if (productItems.size() > 0) {
                        tvNoProductsFound.setVisibility(View.GONE);
                    } else {
                        tvNoProductsFound.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        userViewModel.getUserLoadingState().observe(getViewLifecycleOwner(), new Observer<Contants.UserLoadingState>() {
            @Override
            public void onChanged(Contants.UserLoadingState userLoadingState) {
                switch (userLoadingState) {
                    case LOADING:
                        mProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        mProgressBar.setVisibility(View.GONE);
                        productItemContainer.setVisibility(View.VISIBLE);
                        tvNetworkError.setVisibility(View.GONE);
                        break;
                    case NETWORK_ERROR:
                        mProgressBar.setVisibility(View.GONE);
                        tvNetworkError.setVisibility(View.VISIBLE);
                        productItemContainer.setVisibility(View.GONE);
                        break;
                    case EXPIRED_TOKEN:
                        // Display dialog to inform user to login again.
                        // Here the the code for going to the Login screen. Only happen when the view is visible
                        if (requireView().isShown()) {
                            Bundle inforBundle = new Bundle();
                            inforBundle.putString("title", "Thông báo");
                            inforBundle.putString("message", "Phiên đăng nhập của bạn đã hết hạn");
                            inforBundle.putString("posMessage", "đăng nhập");
                            inforBundle.putString("negMessage", "để sau");
                            inforBundle.putInt("actionId", R.id.action_redirect_confirmation_dialog_to_login_nav);
                            NavHostFragment.findNavController(HomeFragment.this)
                                    .navigate(R.id.action_home_fragment_to_redirect_confirmation_dialog, inforBundle);
                        }
                        break;
                    case NOT_AUTHORIZED:
                        userViewModel.createNewAccessTokenFromRefreshToken();
                }
            }
        });
        userViewModel.getUserActionStateMLiveData().observe(getViewLifecycleOwner(), new Observer<Contants.UserActionState>() {
            @Override
            public void onChanged(Contants.UserActionState userActionStates) {
                switch (userActionStates) {
                    case PROCESSING:
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show();
                        break;
                    case DONE:
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        break;
                    case NETWORK_ERROR:
                        Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        break;
                    case REGAINED_ACCESS_TOKEN:
                        Toast.makeText(requireContext(), "New Access Token Received", Toast.LENGTH_SHORT).show();
                        break;
                    case NOT_AUTHORIZED:
                        Toast.makeText(requireContext(), "Not Authorized", Toast.LENGTH_SHORT).show();
                        Bundle inforBundle = new Bundle();
                        inforBundle.putString("title", "Thông báo");
                        inforBundle.putString("message", "Phiên đăng nhập của bạn đã hết hạn");
                        inforBundle.putString("posMessage", "đăng nhập");
                        inforBundle.putString("negMessage", "để sau");
                        inforBundle.putInt("actionId", R.id.action_redirect_confirmation_dialog_to_login_nav);
                        NavHostFragment.findNavController(HomeFragment.this)
                                .navigate(R.id.action_home_fragment_to_redirect_confirmation_dialog, inforBundle);
                        break;
                }
            }
        });
        Log.i("HomeFragment", "onViewCreated called");

    }

    private void connectViews(View view) {
        productItemContainer = (RecyclerView) view.findViewById(R.id.productItemsContainer);
        loginHintContainer = view.findViewById(R.id.LoginHintContainer);
        mProgressBar = view.findViewById(R.id.pbLoadingBar);
        productItemsOuterContainer = view.findViewById(R.id.productItemsOuterContainer);
        tvNoProductsFound = view.findViewById(R.id.tvNoProductsFound);
        tvNetworkError = view.findViewById(R.id.tvNetworkError);
        btnGoToLoginScreen = view.findViewById(R.id.btnGoToLoginScreen);

        adapter = new ProductItemsAdapter(view.getContext(), R.layout.tracked_product_item);
        adapter.setListener(new ProductItemsAdapter.Listener() {
            @Override
            public void onItemClicked(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("productUrl", url);
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_global_productItemFragment, bundle);
            }
        });
        adapter.setActionListener(new ProductItemsAdapter.ActionListener() {
            @Override
            public void onActionClicked(int action, ProductItem itemToBeActedOn) {
                switch (action) {
                    case ProductItemsAdapter.ACTION_UPDATE_PRICE:
                        mProductItem = itemToBeActedOn;

                        Bundle inforBundle = new Bundle();
                        inforBundle.putInt("wishedPrice", mProductItem.getDesiredPrice());
                        NavHostFragment.findNavController(HomeFragment.this)
                                .navigate(R.id.action_home_fragment_to_add_to_favorite_fragment, inforBundle);
                        break;
                    case ProductItemsAdapter.ACTION_DETETE_ITEM:
                        mProductItem = itemToBeActedOn;

                        userViewModel.deleteTrackedProduct(itemToBeActedOn.getId(),
                                itemToBeActedOn.getPlatform(),
                                userViewModel.getSharedPreferences().getString("accessToken", null));
                        break;
                }
            }
        });
        productItemContainer.setAdapter(adapter);
        productItemContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        btnGoToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_home_fragment_to_login_nav);
            }
        });
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Log.i("HomeFragment", "onAttach called");
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.i("HomeFragment", "onStart called");
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("HomeFragment", "onResume called");
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.i("HomeFragment", "onPause called");
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.i("HomeFragment", "onStop called");
//    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.i("HomeFragment", "onDestroyView called");
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.i("HomeFragment", "onDetach called");
//    }
}