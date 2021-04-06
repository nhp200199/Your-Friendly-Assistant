package com.phucnguyen.khoaluantotnghiep.model.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.phucnguyen.khoaluantotnghiep.model.Review;

public class ReviewDataSourceFactory extends DataSource.Factory<Integer, Review> {

    private MutableLiveData<ReviewDataSource> mReviewDataSourceMutableLiveData;
    private String productId;
    private String platform;
    private String seller;
    private String filter;

    public ReviewDataSourceFactory(String productId, String platform, String seller, String filter) {
        this.productId = productId;
        this.platform = platform;
        this.seller = seller;
        this.filter = filter;
        mReviewDataSourceMutableLiveData = new MutableLiveData<ReviewDataSource>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Review> create() {
        ReviewDataSource reviewDataSource = new ReviewDataSource(productId, platform, seller, filter);
        mReviewDataSourceMutableLiveData.postValue(reviewDataSource);
        return reviewDataSource;
    }

    public MutableLiveData<ReviewDataSource> getReviewDataSourceMutableLiveData() {
        return mReviewDataSourceMutableLiveData;
    }
}
