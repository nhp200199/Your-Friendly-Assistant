package com.phucnguyen.khoaluantotnghiep.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSource;
import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSourceFactory;

public class ReviewRepo {
    private ReviewDataSourceFactory reviewFactory;
    private LiveData<ReviewDataSource.LoadingState> loadingState;

    public ReviewRepo(String productId, String platform, String seller, String filter) {
        reviewFactory = new ReviewDataSourceFactory(productId, platform, seller, filter);
        loadingState = Transformations.switchMap(
                reviewFactory.getReviewDataSourceMutableLiveData(),
                ReviewDataSource::getLoadingState
        );
    }

    public ReviewDataSourceFactory getReviewFactory() {
        return reviewFactory;
    }

    public LiveData<ReviewDataSource.LoadingState> getLoadingState() {
        return loadingState;
    }
}
