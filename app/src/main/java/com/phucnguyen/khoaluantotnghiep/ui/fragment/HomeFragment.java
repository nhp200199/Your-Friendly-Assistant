package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        ProductItemsAdapter adapter = new ProductItemsAdapter(view.getContext(), R.layout.product_item);
        adapter.setListener(new ProductItemsAdapter.Listener() {
            @Override
            public void onItemClicked(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("productUrl", url);
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_global_productItemFragment, bundle);
            }
        });
        productItemContainer.setAdapter(adapter);
        productItemContainer.setLayoutManager(new LinearLayoutManager(getContext()));

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