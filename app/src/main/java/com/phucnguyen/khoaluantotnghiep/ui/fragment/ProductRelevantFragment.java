package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;
import com.phucnguyen.khoaluantotnghiep.viewmodel.RelavantProductViewModel;
import com.phucnguyen.khoaluantotnghiep.viewmodel.RelavantProductViewModelFactory;

import java.util.List;

public class ProductRelevantFragment extends Fragment {
    private RecyclerView productItemsContainer;
    private ProgressBar pbLoadingStatus;
    private TextView tvNoProductsFound;

    private ProductItemViewModel mProductItemViewModel;
    private RelavantProductViewModel mRelavantProductViewModel;
    private GridLayoutManager layoutManager;
    private ProductItemsAdapter mAdapter;

    public ProductRelevantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductItemViewModel = new ViewModelProvider(requireParentFragment()).get(ProductItemViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_relevant, container, false);
        productItemsContainer = (RecyclerView) v.findViewById(R.id.productItemsContainer);
        pbLoadingStatus = (ProgressBar) v.findViewById(R.id.pbLoadingBar);
        tvNoProductsFound = (TextView) v.findViewById(R.id.tvNoProductsFound);

        //setup RecyclerView
        layoutManager = new GridLayoutManager(requireContext(), 2);
        mAdapter = new ProductItemsAdapter(requireContext(), R.layout.product_item_grid);
        mAdapter.setListener(new ProductItemsAdapter.Listener() {
            @Override
            public void onItemClicked(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("productUrl", url);
                NavHostFragment.findNavController(ProductRelevantFragment.this)
                        .navigate(R.id.action_global_productItemFragment, bundle);
            }
        });
        productItemsContainer.setAdapter(mAdapter);
        productItemsContainer.setLayoutManager(layoutManager);

//        mProductItemViewModel.getProductItems().observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
//            @Override
//            public void onChanged(List<ProductItem> productItems) {
//                mAdapter.setProductItems(productItems);
//            }
//        });
        mProductItemViewModel.getProductItem().observe(getViewLifecycleOwner(), new Observer<ProductItem>() {
            @Override
            public void onChanged(ProductItem productItem) {
                if (productItem != null) {
                    RelavantProductViewModelFactory factory = new RelavantProductViewModelFactory(
                            productItem.getId(),
                            productItem.getPlatform(),
                            productItem.getCategoryId(),
                            productItem.getName()
                    );
                    mRelavantProductViewModel = new ViewModelProvider(requireParentFragment(), factory)
                            .get(RelavantProductViewModel.class);
                    mRelavantProductViewModel.getLoadingState().observe(getViewLifecycleOwner(), new Observer<Contants.LoadingState>() {
                        @Override
                        public void onChanged(Contants.LoadingState loadingState) {
                            if (loadingState == Contants.LoadingState.LOADING) {
                                pbLoadingStatus.setVisibility(View.VISIBLE);
                                return;
                            } else pbLoadingStatus.setVisibility(View.INVISIBLE);
                            if (loadingState == Contants.LoadingState.SUCCESS_WITH_NO_VALUES) {
                                productItemsContainer.setVisibility(View.INVISIBLE);
                                tvNoProductsFound.setVisibility(View.VISIBLE);
                            } else if (loadingState == Contants.LoadingState.SUCCESS) {
                                tvNoProductsFound.setVisibility(View.INVISIBLE);
                                productItemsContainer.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    mRelavantProductViewModel.getRelavantProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
                        @Override
                        public void onChanged(List<ProductItem> items) {
                            if (items != null)
                                    mAdapter.setProductItems(items);
                        }
                    });
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}