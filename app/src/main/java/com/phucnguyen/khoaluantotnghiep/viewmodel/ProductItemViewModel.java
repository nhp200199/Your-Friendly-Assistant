package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.model.Price;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.ProductItemResponse;
import com.phucnguyen.khoaluantotnghiep.model.Seller;
import com.phucnguyen.khoaluantotnghiep.repository.ProductItemRepo;

import java.util.List;

public class ProductItemViewModel extends AndroidViewModel {
    private ProductItemRepo mProductItemRepo;

    public ProductItemViewModel(@NonNull Application application) {
        super(application);
        mProductItemRepo = new ProductItemRepo(application);
    }

    public LiveData<ProductItem> getProductItem(){
        return mProductItemRepo.getProductItem();
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
}
