package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.CategoryNamesAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsPagingAdapter;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.ProductItemRepo;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.viewmodel.HotProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class HotProductsFragment extends Fragment {
    private static final String LOGTAG = HotProductsFragment.class.getSimpleName();

    private HotProductViewModel mHotProductViewModel;
    private ProductItemRepo mProductItemDao;

    private RecyclerView categoryNamesContainer;
    private RecyclerView productsContainer;
    private ProgressBar pbLoadingBar;
    private TextView tvLoadingResult;
    private RadioGroup radioPlatformGroup;

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
        pbLoadingBar = (ProgressBar) v.findViewById(R.id.pbLoadingBar);
        tvLoadingResult = (TextView) v.findViewById(R.id.tvLoadingResult);
        radioPlatformGroup = (RadioGroup) v.findViewById(R.id.radioPlatformGroup);
        //adapter for the category names
        CategoryNamesAdapter categoryNamesAdapter = new CategoryNamesAdapter(requireContext());
        categoryNamesAdapter.setListener(new CategoryNamesAdapter.Listener() {
            @Override
            public void onCategoryClicked(int categoryPos, String categoryName) {
                mHotProductViewModel.setSelectedCategoryPos(categoryPos);
                mHotProductViewModel.setDataSourceWithNewCategory(categoryName);
            }
        });
        categoryNamesContainer.setAdapter(categoryNamesAdapter);
        categoryNamesContainer.setLayoutManager(new LinearLayoutManager(requireContext(),
                RecyclerView.HORIZONTAL, false));
        //adapter for the products
        ProductItemsPagingAdapter productItemsAdapter = new ProductItemsPagingAdapter(requireContext());
        productsContainer.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        productsContainer.setAdapter(productItemsAdapter);
        productItemsAdapter.setListener(new ProductItemsAdapter.Listener() {
            @Override
            public void onItemClicked(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("productUrl", url);
                NavHostFragment.findNavController(HotProductsFragment.this)
                        .navigate(R.id.action_global_productItemFragment, bundle);
            }
        });

        radioPlatformGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String checkedPlatform = null;
                switch (i) {
                    case R.id.radioAll:
                        checkedPlatform = "all";
                        break;
                    case R.id.radioTiki:
                        checkedPlatform = "tiki";
                        break;
                    case R.id.radioShopee:
                        checkedPlatform = "shopee";
                        break;
                }
                mHotProductViewModel.setDataSourceWithNewPlatform(checkedPlatform);
            }
        });

        mHotProductViewModel.getHotProducts().observe(getViewLifecycleOwner(), new Observer<PagedList<ProductItem>>() {
            @Override
            public void onChanged(PagedList<ProductItem> productItems) {
                productItemsAdapter.submitList(productItems);
            }
        });
        mHotProductViewModel.getLoadingState().observe(getViewLifecycleOwner(), new Observer<Contants.LoadingState>() {
            @Override
            public void onChanged(Contants.LoadingState loadingState) {
                if (loadingState == Contants.LoadingState.FIRST_LOADING) {
                    pbLoadingBar.setVisibility(View.VISIBLE);
                    return;
                } else pbLoadingBar.setVisibility(View.INVISIBLE);
                if (loadingState == Contants.LoadingState.SUCCESS_WITH_NO_VALUES ||
                        loadingState == Contants.LoadingState.ERROR) {
                    tvLoadingResult.setVisibility(View.VISIBLE);
                    productsContainer.setVisibility(View.INVISIBLE);
                    if (loadingState == Contants.LoadingState.SUCCESS_WITH_NO_VALUES) {
                        tvLoadingResult.setText("Chưa có sản phẩm nào trong danh mục này để gợi ý cho bạn");
                    } else
                        tvLoadingResult.setText("Đã có lỗi xảy ra");
                } else {
                    tvLoadingResult.setVisibility(View.INVISIBLE);
                    productsContainer.setVisibility(View.VISIBLE);
                }
                productItemsAdapter.setLoadingState(loadingState);
            }
        });
//        List<String> dummyCategories = new ArrayList<String>();
//        dummyCategories.add("first category");
//        dummyCategories.add("Smartphone");
//        dummyCategories.add("Máy tính bảng");
//        dummyCategories.add("fourth category");
//        dummyCategories.add("fifth category");
//        dummyCategories.add("sixth category");
//        dummyCategories.add("seventh category");
//        dummyCategories.add("eighth category");
//        dummyCategories.add("ninth category");
//        categoryNamesAdapter.setCategories(dummyCategories);
        mHotProductViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> categoriesString) {
                if (categoriesString != null) {
                    categoriesString.add(0, "Tất cả");
                    radioPlatformGroup.setVisibility(View.VISIBLE);
                    categoryNamesAdapter.setCategories(categoriesString);
                } else radioPlatformGroup.setVisibility(View.GONE);
            }
        });

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