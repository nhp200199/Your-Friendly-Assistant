package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.CategoryNamesAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.ProductItemRepo;
import com.phucnguyen.khoaluantotnghiep.viewmodel.HotProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class HotProductsFragment extends Fragment {
    private static final String LOGTAG = HotProductsFragment.class.getSimpleName();

    private HotProductViewModel mHotProductViewModel;
    private ProductItemRepo mProductItemDao;

    private RecyclerView categoryNamesContainer;
    private RecyclerView productsContainer;

    public HotProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotProductViewModel = new ViewModelProvider(this).get(HotProductViewModel.class);
        mProductItemDao = new ProductItemRepo(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hot_products, container, false);
        categoryNamesContainer = (RecyclerView) v.findViewById(R.id.categoryNamesContainer);
        productsContainer = (RecyclerView) v.findViewById(R.id.productsContainer);
        //adapter for the category names
        CategoryNamesAdapter categoryNamesAdapter = new CategoryNamesAdapter(requireContext());
        categoryNamesAdapter.setListener(new CategoryNamesAdapter.Listener() {
            @Override
            public void onCategoryClicked(int categoryPos) {
                mHotProductViewModel.setSelectedCategoryPos(categoryPos);
            }
        });
        categoryNamesContainer.setAdapter(categoryNamesAdapter);
        categoryNamesContainer.setLayoutManager(new LinearLayoutManager(requireContext(),
                RecyclerView.HORIZONTAL, false));
        //adapter for the products
        ProductItemsAdapter productItemsAdapter = new ProductItemsAdapter(requireContext(), R.layout.product_item);
        productsContainer.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        productsContainer.setAdapter(productItemsAdapter);
        mProductItemDao.getProductItems().observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> items) {
                productItemsAdapter.setProductItems(items);
            }
        });
        List<String> dummyCategories = new ArrayList<String>();
        dummyCategories.add("first category");
        dummyCategories.add("second category");
        dummyCategories.add("third category");
        dummyCategories.add("fourth category");
        dummyCategories.add("fifth category");
        dummyCategories.add("sixth category");
        dummyCategories.add("seventh category");
        dummyCategories.add("eighth category");
        dummyCategories.add("ninth category");
        categoryNamesAdapter.setCategories(dummyCategories);

        mHotProductViewModel.getSelectedCategoryPos().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                categoryNamesAdapter.setCurrentCategoryPos(integer);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}