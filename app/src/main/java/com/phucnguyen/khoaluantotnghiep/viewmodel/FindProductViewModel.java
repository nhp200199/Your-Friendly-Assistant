package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.FindProductRepo;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.ArrayList;
import java.util.List;

public class FindProductViewModel extends AndroidViewModel {
    private FindProductRepo repo;
    private LiveData<List<String>> recentSearchs;
    private MutableLiveData<String> queryLiveData = new MutableLiveData<String>();
    private LiveData<List<ProductItem>> suggestedProducts = Transformations.switchMap(
            queryLiveData, queryString -> repo.getProductsForQueryString(queryString, "all")
    );
    private LiveData<Contants.LoadingState> loadingState;

    public FindProductViewModel(@NonNull Application application) {
        super(application);
        repo = new FindProductRepo(application);
        recentSearchs = Transformations.map(repo.getRecentSearchs(), recentSearchs -> {
            List<String> recentSearchStrings = new ArrayList<String>();
            for (RecentSearch search :
                    recentSearchs) {
                recentSearchStrings.add(search.searchContent);
            }
            return recentSearchStrings;
        });
        loadingState = repo.getLoadingState();
    }

    public LiveData<List<String>> getRecentSearchs() {
        return recentSearchs;
    }

    public void setQuery(String query) {
        queryLiveData.setValue(query);
    }

    public LiveData<List<ProductItem>> getSuggestedProducts() {
        return suggestedProducts;
    }

    public LiveData<Contants.LoadingState> getLoadingState() {
        return loadingState;
    }
}
