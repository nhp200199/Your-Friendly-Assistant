package com.phucnguyen.khoaluantotnghiep.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.model.CategoriesResponse;
import com.phucnguyen.khoaluantotnghiep.model.Category;
import com.phucnguyen.khoaluantotnghiep.model.datasource.HotProductsDataSource;
import com.phucnguyen.khoaluantotnghiep.model.datasource.HotProductsDataSourceFactory;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotProductsRepo {
    private ProductItemService service;
    private MutableLiveData<List<Category>> categories;
    private HotProductsDataSourceFactory mHotProductsDataSourceFactory;
    private LiveData<Contants.LoadingState> loadingState;
    private Call<CategoriesResponse> retryRetrofitCall = null;

    public HotProductsRepo(String platform, String category) {
        service = RetrofitInstance.getProductItemService();
        categories = new MutableLiveData<List<Category>>();
        mHotProductsDataSourceFactory = new HotProductsDataSourceFactory(platform, category);
        loadingState = Transformations.switchMap(
                mHotProductsDataSourceFactory.getHotProductsDataSourceMutableLiveData(),
                HotProductsDataSource::getLoadingState
        );
    }

    public HotProductsDataSourceFactory getHotProductsDataSourceFactory() {
        return mHotProductsDataSourceFactory;
    }

    public String getCurrentCategory() {
        return mHotProductsDataSourceFactory.getCategory();
    }

    public void setPlatformForFactory(String newPlatform) {
        mHotProductsDataSourceFactory.setPlatform(newPlatform);
    }

    public void setCategoryForFactory(String newCategory) {
        mHotProductsDataSourceFactory.setCategory(newCategory);
    }

    public LiveData<Contants.LoadingState> getLoadingState() {
        return loadingState;
    }

    public LiveData<List<Category>> getCategories() {
        service.getAllCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful()){
                    categories.postValue(response.body().getCagories());
                    retryRetrofitCall = null;
                }
                else {
                    categories.postValue(null);
                    retryRetrofitCall = call.clone();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                categories.postValue(null);
                retryRetrofitCall = call.clone();
            }
        });
        return categories;
    }

    public Call<CategoriesResponse> getRetryRetrofitCall() {
        return retryRetrofitCall;
    }

    public void setRetryRetrofitCall(Call<CategoriesResponse> retryRetrofitCall) {
        this.retryRetrofitCall = retryRetrofitCall;
    }

    public void retryLoadingCategories(){
        retryRetrofitCall.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful()){
                    categories.postValue(response.body().getCagories());
                    retryRetrofitCall = null;
                }
                else {
                    categories.postValue(null);
                    retryRetrofitCall = call.clone();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                categories.postValue(null);
                retryRetrofitCall = call.clone();
            }
        });
    }
}
