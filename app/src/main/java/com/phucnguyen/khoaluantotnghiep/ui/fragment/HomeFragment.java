package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.ProductItemRepo;

public class HomeFragment extends Fragment {
    private static final String LOGTAG = HomeFragment.class.getSimpleName();
    private RecyclerView productItemContainer;
    private ProductItemRepo mProductItemRepo;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductItemRepo = new ProductItemRepo(getContext());
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

        ProductItemsAdapter adapter = new ProductItemsAdapter(view.getContext());
        productItemContainer.setAdapter(adapter);
        productItemContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductItemRepo.getProductItems().observe(requireActivity(), new Observer<PagedList<ProductItem>>() {
            @Override
            public void onChanged(PagedList<ProductItem> productItems) {
                adapter.submitList(productItems);
            }
        });
    }

    private void connectViews(View view) {
        productItemContainer = (RecyclerView) view.findViewById(R.id.productItemsContainer);
    }
}