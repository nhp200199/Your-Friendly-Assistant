package com.phucnguyen.khoaluantotnghiep.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.repository.RelavantProductRepo;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.List;

public class RelavantProductViewModel extends ViewModel {
    private RelavantProductRepo repo;
    private LiveData<List<ProductItem>> relavantProducts;
    private LiveData<Contants.LoadingState> loadingState;

    public RelavantProductViewModel(String id, String categoryId, String name, String platform) {
        repo = new RelavantProductRepo(id, name, categoryId, platform);
        relavantProducts = repo.getProducts();
        loadingState = repo.getLoadingState();
    }

    public LiveData<List<ProductItem>> getRelavantProducts() {
        return relavantProducts;
    }

    public LiveData<Contants.LoadingState> getLoadingState() {
        return loadingState;
    }
}
