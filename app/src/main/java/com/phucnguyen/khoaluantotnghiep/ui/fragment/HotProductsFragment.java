package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.adapters.CategoryNamesAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsAdapter;
import com.phucnguyen.khoaluantotnghiep.adapters.ProductItemsPagingAdapter;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.viewmodel.HotProductViewModel;

import java.util.List;

public class HotProductsFragment extends Fragment {
    private static final String LOGTAG = HotProductsFragment.class.getSimpleName();

    private HotProductViewModel mHotProductViewModel;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView categoryNamesContainer;
    private RecyclerView productsContainer;
    private ProgressBar pbLoadingBar;
    private TextView tvLoadingResult;
    private RadioGroup radioPlatformGroup;
    private RadioButton radioAll;
    private RadioButton radioTiki;
    private RadioButton radioShopee;

    public HotProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotProductViewModel = new ViewModelProvider(this).get(HotProductViewModel.class);
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
        radioAll = (RadioButton) v.findViewById(R.id.radioAll);
        radioTiki = (RadioButton) v.findViewById(R.id.radioTiki);
        radioShopee = (RadioButton) v.findViewById(R.id.radioShopee);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHotProductViewModel.refreshDataSource();
                //only retry loading categories when previous call to receive categories has failed
                mHotProductViewModel.retryLoadingCategories();
                refreshLayout.setRefreshing(false);
            }
        });

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
        productItemsAdapter.setBtnListener(new ProductItemsPagingAdapter.btnListener() {
            @Override
            public void onRetry() {
                mHotProductViewModel.retryLoadingSubPages();
            }
        });

        //use a variable to track which radio button's id is currently clicked
        int checkedRadioPlatformId = mHotProductViewModel.getCurrentRadioPlatformId();
        radioPlatformGroup.check(checkedRadioPlatformId);
        radioAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedRadioPlatformId != view.getId()){
                    mHotProductViewModel.setCurrentRadioPlatformId(view.getId());
                    mHotProductViewModel.setDataSourceWithNewPlatform("all");
                }
            }
        });
        radioTiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedRadioPlatformId != view.getId()){
                    mHotProductViewModel.setCurrentRadioPlatformId(view.getId());
                    mHotProductViewModel.setDataSourceWithNewPlatform("tiki");
                }
            }
        });
        radioShopee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedRadioPlatformId != view.getId()){
                    mHotProductViewModel.setCurrentRadioPlatformId(view.getId());
                    mHotProductViewModel.setDataSourceWithNewPlatform("shopee");
                }
            }
        });
        //Using radioPlatformGroup.setOnCheckedChangeListener(...) to track the currently selected radio button
        //cause an issue: onCheckChanged does not invoke like the way we want. Should use the implementation above
//        radioPlatformGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                String checkedPlatform = null;
//                switch (i) {
//                    case R.id.radioAll:
//                        checkedPlatform = "all";
//                        break;
//                    case R.id.radioTiki:
//                        checkedPlatform = "tiki";
//                        break;
//                    case R.id.radioShopee:
//                        checkedPlatform = "shopee";
//                        break;
//                }
//                RadioButton radioButton = radioGroup.getRootView().findViewById(radioGroup.getCheckedRadioButtonId());
//                if (!mHotProductViewModel.getCurrentPlatform().equals(radioButton.getText().toString())){
//                    mHotProductViewModel.setDataSourceWithNewPlatform(checkedPlatform);
//                    mHotProductViewModel.setCurrentPlatform(checkedPlatform);
//                }
//            }
//        });

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
                        loadingState == Contants.LoadingState.FIRST_LOAD_ERROR) {
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

        mHotProductViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> categoriesString) {
                if (categoriesString != null) {
                    //shouldn't change the list emitted from the live data. Here we have to do that
                    //because we want to include "Tất cả" in our categories
                    if (!categoriesString.contains("Tất cả"))
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