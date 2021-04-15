package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.util.SparseIntArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.phucnguyen.khoaluantotnghiep.model.Review;
import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSource;
import com.phucnguyen.khoaluantotnghiep.model.datasource.ReviewDataSourceFactory;
import com.phucnguyen.khoaluantotnghiep.repository.ReviewRepo;

public class ReviewViewModel extends ViewModel {
    private SparseIntArray nestedRecyclerViewPositions;
    private String productId;
    private String platform;
    private String seller;
    private MutableLiveData<ReviewRepo> repoLiveData = new MutableLiveData<ReviewRepo>();
    private MutableLiveData<String> filterMutableLiveData = new MutableLiveData<String>("all");
    //here, we calculate the pagedList based on the filter string. Note
    //that when the filter string changes, pagedList will observe to a
    //different LiveData<PagedList<>>. It will automatically unsubcribe on the previous LiveData
    private LiveData<PagedList<Review>> pagedList = Transformations.switchMap(
            filterMutableLiveData, filter -> {
                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .build();
                ReviewRepo repo = new ReviewRepo(productId, platform, seller, filter);
                repoLiveData.postValue(repo);
                return new LivePagedListBuilder<Integer, Review>(repo.getReviewFactory(), config)
                        .build();
            }
    );
    private LiveData<ReviewDataSource.LoadingState> loadingState = Transformations.switchMap(
            repoLiveData, ReviewRepo::getLoadingState
    );

    public ReviewViewModel(String productId, String platform, String seller) {
        this.productId = productId;
        this.platform = platform;
        this.seller = seller;
        nestedRecyclerViewPositions = new SparseIntArray();
    }

    public LiveData<PagedList<Review>> getPagedList() {
        return pagedList;
    }

    public MutableLiveData<String> getFilterMutableLiveData() {
        return filterMutableLiveData;
    }

    public void setFilterMutableLiveData(String filter) {
        filterMutableLiveData.setValue(filter);
    }

    public LiveData<ReviewDataSource.LoadingState> getLoadingState() {
        return loadingState;
    }

    public void setNestedRecyclerViewPositions(int position, int value) {
        nestedRecyclerViewPositions.put(position, value);
    }

    public SparseIntArray getNestedRecyclerViewPositions() {
        return nestedRecyclerViewPositions;
    }
}
