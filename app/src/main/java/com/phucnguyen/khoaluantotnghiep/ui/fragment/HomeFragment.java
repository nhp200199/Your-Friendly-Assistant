package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.ProductItemRepo;

import java.util.List;

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
        Log.i("HomeFragment", "onCreate called");
        setHasOptionsMenu(true);
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
        productItemContainer.setAdapter(adapter);
        productItemContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductItemRepo.getProductItems().observe(requireActivity(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {
                adapter.setProductItems(productItems);
            }
        });
        Log.i("HomeFragment", "onViewCreated called");

    }

    private void connectViews(View view) {
        productItemContainer = (RecyclerView) view.findViewById(R.id.productItemsContainer);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_for_real, menu);
        menu.clear();
    }
}