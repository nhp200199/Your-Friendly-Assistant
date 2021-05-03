package com.phucnguyen.khoaluantotnghiep.model.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.phucnguyen.khoaluantotnghiep.model.response.HotItemsResponse;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotProductsDataSource extends PageKeyedDataSource<Integer, ProductItem> {
    private ProductItemService service;
    private String platform;
    private String category;
    private MutableLiveData<Contants.LoadingState> loadingState;
    private LoadParams<Integer> params;
    private LoadCallback<Integer, ProductItem> callback;

    public HotProductsDataSource(String platform, String category) {
        this.platform = platform;
        this.category = category;
        service = RetrofitInstance.getProductItemService();
        loadingState = new MutableLiveData<Contants.LoadingState>();
    }

    public MutableLiveData<Contants.LoadingState> getLoadingState() {
        return loadingState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ProductItem> callback) {
        final int page = 1;
        loadingState.postValue(Contants.LoadingState.FIRST_LOADING);
        service.getHotProducts(platform, category, page).enqueue(new Callback<HotItemsResponse>() {
            @Override
            public void onResponse(Call<HotItemsResponse> call, Response<HotItemsResponse> response) {
                if (response.isSuccessful()) {
                    List<ProductItem> items = response.body().getItems();
                    if (items.size() > 0) {
                        loadingState.postValue(Contants.LoadingState.SUCCESS);
                        callback.onResult(response.body().getItems(), 0, page + 1);
                    } else {
                        loadingState.postValue(Contants.LoadingState.SUCCESS_WITH_NO_VALUES);
                        callback.onResult(new ArrayList<ProductItem>(), 0, 0);
                    }
                } else {
                    loadingState.postValue(Contants.LoadingState.FIRST_LOAD_ERROR);
                    callback.onResult(new ArrayList<ProductItem>(), 0, 0);
                }
            }

            @Override
            public void onFailure(Call<HotItemsResponse> call, Throwable t) {
                loadingState.postValue(Contants.LoadingState.FIRST_LOAD_ERROR);
                callback.onResult(new ArrayList<ProductItem>(), 0, 0);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ProductItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ProductItem> callback) {
        this.params = params;
        this.callback = callback;
        final int currentPage = params.key;
        loadingState.postValue(Contants.LoadingState.LOADING);
        service.getHotProducts(platform, category, currentPage).enqueue(new Callback<HotItemsResponse>() {
            @Override
            public void onResponse(Call<HotItemsResponse> call, Response<HotItemsResponse> response) {
                if (response.isSuccessful()) {
                    loadingState.postValue(Contants.LoadingState.SUCCESS);
                    int lastPage = response.body().getPagination().getLastPage();
                    Integer nextPageKey = params.key == lastPage ? null : currentPage + 1;
                    List<ProductItem> items = response.body().getItems();
                    callback.onResult(items, nextPageKey);
                } else {
                    loadingState.postValue(Contants.LoadingState.SUB_LOAD_ERROR);
                    //when the load is fail, dont call onResult() on the call back,
                    //just ignore it, update the loading state for the UI to handle reload

                    //callback.onResult(new ArrayList<ProductItem>(), currentPage);
                }
            }

            @Override
            public void onFailure(Call<HotItemsResponse> call, Throwable t) {
                loadingState.postValue(Contants.LoadingState.SUB_LOAD_ERROR);
                //when the load is fail, dont call onResult() on the call back,
                //just ignore it, update the loading state for the UI to handle reload

                //callback.onResult(new ArrayList<ProductItem>(), currentPage);
            }
        });
    }
    public void retryLoadingSubPages(){
        loadAfter(params, callback);
    }
}
