package com.phucnguyen.khoaluantotnghiep.model.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.phucnguyen.khoaluantotnghiep.model.Review;
import com.phucnguyen.khoaluantotnghiep.model.response.ReviewResponse;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDataSource extends PageKeyedDataSource<Integer, Review> {
    public enum LoadingState {
        FIRST_LOADING,
        LOADING,
        SUCCESS_WITH_NO_VALUES,
        SUCCESS,
        ERROR
    }

    private ProductItemService service;
    private String productId;
    private Map<String, String> queryOptions;
    private MutableLiveData<LoadingState> loadingState;

    public ReviewDataSource(String productId,
                            String platform,
                            String seller,
                            String filter) {
        service = RetrofitInstance.getProductItemService();
        this.productId = productId;
        queryOptions = new HashMap<String, String>();
        queryOptions.put("platform", platform);
        queryOptions.put("seller", seller);
        //dont include the "filter" query if user wants to see all reviews
        if (!filter.equals("all"))
            queryOptions.put("filter", filter);

        loadingState = new MutableLiveData<LoadingState>();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Review> callback) {
        final int currentPage = 1;
        loadingState.postValue(LoadingState.FIRST_LOADING);
        queryOptions.put("currentPage", String.valueOf(currentPage));
        service.getReview(productId,
                queryOptions).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    //we can only pass the items if there is reviews
                    //if the product has no review, return empty list
                    //so that no further load will be processed. Note: you have to specify a new array list
                    //instead of a list with 0 value
                    if (response.body().getReviews().size() > 0) {
                        loadingState.postValue(LoadingState.SUCCESS);
                        int lastPage = response.body().getPagination().getLastPage();
                        Integer nextPageKey = currentPage == lastPage ? null : currentPage + 1;
                        callback.onResult(response.body().getReviews(), null, nextPageKey);
                    } else {
                        loadingState.postValue(LoadingState.SUCCESS_WITH_NO_VALUES);
                        callback.onResult(new ArrayList<Review>(), null, currentPage);
                    }
                } else {
                    loadingState.postValue(LoadingState.ERROR);
                    //since no data was fetched we pass empty list and dont increment the currentPage number
                    //so that it can retry the fetching of 1st currentPage
                    callback.onResult(new ArrayList<>(), null, currentPage);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                loadingState.postValue(LoadingState.ERROR);
                callback.onResult(new ArrayList<>(), null, currentPage);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Review> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Review> callback) {
        loadingState.postValue(LoadingState.LOADING);
        int currentPage = params.key;
        queryOptions.put("currentPage", String.valueOf(currentPage));
        service.getReview(productId,
                queryOptions).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    int lastPage = response.body().getPagination().getLastPage();
                    Integer nextPageKey = params.key == lastPage ? null : params.key + 1;
                    loadingState.postValue(LoadingState.SUCCESS);
                    callback.onResult(response.body().getReviews(), nextPageKey);
                } else {
                    //setting load state so that the UI can know data fetching failed
                    loadingState.postValue(LoadingState.ERROR);

                    //since no data was fetched we pass empty list and dont increment the currentPage number
                    //so that it can retry the fetching of 1st currentPage
                    callback.onResult(new ArrayList<>(), currentPage);

                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                //setting load state so that the UI can know data fetching failed
                loadingState.postValue(LoadingState.ERROR);
                callback.onResult(new ArrayList<>(), currentPage);
            }
        });
    }

    public LiveData<LoadingState> getLoadingState() {
        return loadingState;
    }
}
