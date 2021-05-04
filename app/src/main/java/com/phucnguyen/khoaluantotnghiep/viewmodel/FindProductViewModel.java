package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.database.RecentSearch;
import com.phucnguyen.khoaluantotnghiep.model.Category;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.FindProductRepo;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindProductViewModel extends AndroidViewModel {
    private FindProductRepo repo;
    private LiveData<List<String>> recentSearchs;
    private MutableLiveData<String> queryLiveData = new MutableLiveData<String>();
    private MutableLiveData<String> platformLiveData = new MutableLiveData<String>();
    private MediatorLiveData<Map<String, String>> mMediatorLiveData = new MediatorLiveData<Map<String, String>>();
    //private LiveData<List<ProductItem>> suggestedProducts;
    private LiveData<List<ProductItem>> suggestedProducts = Transformations.switchMap(
            mMediatorLiveData, optionsMap -> repo.getProductItems(optionsMap.get("query"),
                    optionsMap.get("platform"), optionsMap.get("category"))
    );
    private LiveData<Contants.ItemLoadingState> loadingState;
    private LiveData<List<Category>> categories;
    private MutableLiveData<String> categoryLiveData = new MutableLiveData<String>();
    private int currentCategoryMenuItem;

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
        mMediatorLiveData.addSource(queryLiveData, query -> {
            Map<String, String> optionsMap = new HashMap<String, String>();
            optionsMap.put("query", query);
            optionsMap.put("platform", platformLiveData.getValue());
            optionsMap.put("category", categoryLiveData.getValue());
            mMediatorLiveData.setValue(optionsMap);
        });
        mMediatorLiveData.addSource(platformLiveData, platform -> {
            Map<String, String> optionsMap = new HashMap<String, String>();
            optionsMap.put("query", queryLiveData.getValue());
            optionsMap.put("platform", platform);
            optionsMap.put("category", categoryLiveData.getValue());
            mMediatorLiveData.setValue(optionsMap);
        });
        mMediatorLiveData.addSource(categoryLiveData, category -> {
            Map<String, String> optionsMap = new HashMap<String, String>();
            optionsMap.put("query", queryLiveData.getValue());
            optionsMap.put("platform", platformLiveData.getValue());
            optionsMap.put("category", category);
            mMediatorLiveData.setValue(optionsMap);
        });
        loadingState = repo.getLoadingState();
        categories = repo.getCategories();
    }

    public LiveData<List<String>> getRecentSearchs() {
        return recentSearchs;
    }

    public void setQuery(String query) {
        queryLiveData.setValue(query);
    }

//    public LiveData<List<ProductItem>> getSuggestedProducts() {
//        return suggestedProducts;
//    }


    public void setPlatform(String platform) {
        platformLiveData.setValue(platform);
    }

    public LiveData<Contants.ItemLoadingState> getLoadingState() {
        return loadingState;
    }

    public LiveData<List<ProductItem>> getSuggestedProducts() {
        return suggestedProducts;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public int getCurrentCategory() {
        return currentCategoryMenuItem;
    }

    public void setCurrentCategory(int categoryId) {
        this.currentCategoryMenuItem = categoryId;
    }

    public MutableLiveData<String> getCategoryLiveData() {
        return categoryLiveData;
    }

    public void setCategoryLiveData(String category) {
        this.categoryLiveData.setValue(category);
    }
}
