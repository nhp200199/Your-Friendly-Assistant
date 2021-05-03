package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.database.RecentSearchDao;
import com.phucnguyen.khoaluantotnghiep.database.SearchDatabase;
import com.phucnguyen.khoaluantotnghiep.model.Category;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.response.SuggestedProductsResponse;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindProductRepo {
    private RecentSearchDao mRecentSearchDao;
    private LiveData<List<RecentSearch>> recentSearchs;
    private ProductItemService service;
    private MutableLiveData<List<ProductItem>> suggestedProducts;
    private MutableLiveData<Contants.LoadingState> loadingState;
    private MutableLiveData<List<Category>> categories;

    public FindProductRepo(Context context) {
        mRecentSearchDao = SearchDatabase.getInstance(context)
                .recentSearchDao();
        recentSearchs = mRecentSearchDao.getRecentSearchsForHistory();
        service = RetrofitInstance.getProductItemService();
        suggestedProducts = new MutableLiveData<List<ProductItem>>();
        loadingState = new MutableLiveData<Contants.LoadingState>();
        categories = new MutableLiveData<List<Category>>();
    }

    private void callToReceiveSuggestedProducts(String query, String platform, String category) {
        loadingState.postValue(Contants.LoadingState.LOADING);
        service.getSuggestedItems(query, platform, category).enqueue(new Callback<SuggestedProductsResponse>() {
            @Override
            public void onResponse(Call<SuggestedProductsResponse> call, Response<SuggestedProductsResponse> response) {
                if (response.isSuccessful()) {
                    loadingState.postValue(Contants.LoadingState.SUCCESS);
                    suggestedProducts.postValue(response.body().getItems());
                    //only query for the categories when user requires it (category.equals("tất cả"))
                    if (response.body().getCategories() != null)
                        categories.postValue(response.body().getCategories());
                } else {
                    loadingState.postValue(Contants.LoadingState.FIRST_LOAD_ERROR);
                    suggestedProducts.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SuggestedProductsResponse> call, Throwable t) {
                loadingState.postValue(Contants.LoadingState.FIRST_LOAD_ERROR);
                suggestedProducts.postValue(null);
            }
        });
    }

    public LiveData<List<RecentSearch>> getRecentSearchs() {
        return recentSearchs;
    }

    public LiveData<List<ProductItem>> getProductItems(String query, String platform, String category) {
        callToReceiveSuggestedProducts(query, platform, category);
        return suggestedProducts;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<Contants.LoadingState> getLoadingState() {
        return loadingState;
    }
}
