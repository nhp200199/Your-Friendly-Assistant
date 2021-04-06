package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.viewmodel.ProductItemViewModel;

public class ProductRelevantFragment extends Fragment {
    private RecyclerView productItemsContainer;
    private TextView tvLoadStatus;

    private ProductItemViewModel mProductItemViewModel;
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
        tvLoadStatus = (TextView) v.findViewById(R.id.tvLoadingStatus);

        //setup RecyclerView
        layoutManager = new GridLayoutManager(requireContext(), 2);
        mAdapter = new ProductItemsAdapter(requireContext(), R.layout.product_item_grid);
        productItemsContainer.setAdapter(mAdapter);
        productItemsContainer.setLayoutManager(layoutManager);

        mProductItemViewModel.getProductItems().observe(getViewLifecycleOwner(), new Observer<PagedList<ProductItem>>() {
            @Override
            public void onChanged(PagedList<ProductItem> productItems) {
                mAdapter.submitList(productItems);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}