package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.phucnguyen.khoaluantotnghiep.model.Price;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.ProductItemResponse;
import com.phucnguyen.khoaluantotnghiep.model.Seller;
import com.phucnguyen.khoaluantotnghiep.repository.ProductItemRepo;

import java.util.List;

public class ProductItemViewModel extends AndroidViewModel {
    private ProductItemRepo mProductItemRepo;
    private LiveData<ProductItem> productItem;
    private LiveData<List<ProductItem>> productItems;

    public ProductItemViewModel(@NonNull Application application) {
        super(application);
        mProductItemRepo = new ProductItemRepo(application);
        productItems = mProductItemRepo.getProductItems();
        productItem = mProductItemRepo.getProductItem();
    }

    public LiveData<ProductItem> getProductItem(){
        return productItem;
    }

    public LiveData<Seller> getSeller(){
        return mProductItemRepo.getSeller();
    }

    public void makeApiCallToReceiveProductItem(String url, String include){
        mProductItemRepo.getItem(url, include);
    }

    public LiveData<ProductItemResponse> getProductItemResponse(){
        return mProductItemRepo.getProductItemResponse();
    }
    public LiveData<List<Price>> getProductPriceHistory(){
        return mProductItemRepo.getProductPriceHistory();
    }

    public LiveData<List<ProductItem>> getProductItems() {
        return productItems;
    }
}
