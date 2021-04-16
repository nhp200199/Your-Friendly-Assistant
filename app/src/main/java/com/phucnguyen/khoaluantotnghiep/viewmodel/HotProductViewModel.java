package com.phucnguyen.khoaluantotnghiep.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.phucnguyen.khoaluantotnghiep.model.Category;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.HotProductsRepo;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.ArrayList;
import java.util.List;

public class HotProductViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedCategoryPos;
    private LiveData<List<String>> categories;
    private HotProductsRepo repo;
    private LiveData<PagedList<ProductItem>> hotProducts;

    public HotProductViewModel() {
        selectedCategoryPos = new MutableLiveData<Integer>(0); //default value is 0
        repo = new HotProductsRepo("all", "Tất cả");
        categories = Transformations.map(repo.getCategories(), categories -> {
            if (categories == null)
                return null;
            List<String> categoriesStrings = new ArrayList<String>();
            for (Category category : categories) {
                categoriesStrings.add(category.getName());
            }
            return categoriesStrings;
        });
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();
        hotProducts = new LivePagedListBuilder<Integer, ProductItem>(repo.getHotProductsDataSourceFactory(),
                config).build();
    }

    public LiveData<Integer> getSelectedCategoryPos() {
        return selectedCategoryPos;
    }

    public void setSelectedCategoryPos(int selectedPos) {
        selectedCategoryPos.setValue(selectedPos);
    }

    public LiveData<PagedList<ProductItem>> getHotProducts() {
        return hotProducts;
    }

    public void setDataSourceWithNewCategory(String newCategory) {
        String currentCategory = repo.getCurrentCategory();
        if (!currentCategory.equals(newCategory)) {
            repo.setCategoryForFactory(newCategory);
            hotProducts.getValue().getDataSource().invalidate();
        }
    }

    public void setDataSourceWithNewPlatform(String newPlatform) {
        repo.setPlatformForFactory(newPlatform);
        hotProducts.getValue().getDataSource().invalidate();
    }

    public LiveData<Contants.LoadingState> getLoadingState() {
        return repo.getLoadingState();
    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }
}
