package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.database.AppDatabase;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.Price;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.response.ProductItemResponse;
import com.phucnguyen.khoaluantotnghiep.model.Seller;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductItemRepo {
    private ProductItemDao mProductItemDao;
    private LiveData<List<ProductItem>> mProductItems;
    private ProductItemService mProductItemService;
    private MutableLiveData<ProductItemResponse> mProductItemResponseMutableLiveData;

    public ProductItemRepo(Context context) {
        mProductItemDao = AppDatabase.getInstance(context).getProductItemDao();
        mProductItems = mProductItemDao.getAllProducts();
        mProductItemService = RetrofitInstance.getProductItemService();
        mProductItemResponseMutableLiveData = new MutableLiveData<ProductItemResponse>();
    }

    public LiveData<List<ProductItem>> getProductItems() {
        return mProductItems;
    }

    public void getItem(String url, String include) {
        Call<ProductItemResponse> productItem = mProductItemService.getItem(url, include);
        productItem.enqueue(new Callback<ProductItemResponse>() {
            @Override
            public void onResponse(Call<ProductItemResponse> call, Response<ProductItemResponse> response) {
                if (response.isSuccessful())
                    mProductItemResponseMutableLiveData.postValue(response.body());
                else mProductItemResponseMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<ProductItemResponse> call, Throwable t) {
                mProductItemResponseMutableLiveData.postValue(null);
                t.printStackTrace();
            }
        });
    }

    public LiveData<ProductItemResponse> getProductItemResponse() {
        return mProductItemResponseMutableLiveData;
    }

    public LiveData<ProductItem> getProductItem() {
        return Transformations.map(
                mProductItemResponseMutableLiveData,
                (response) -> response == null ? null : response.getProductItem()
        );
    }

    public LiveData<Seller> getSeller() {
        return Transformations.map(
                mProductItemResponseMutableLiveData,
                (response) -> {
                    if (response == null)
                        return null;
                    else {
                        Seller seller = response.getSeller();
                        seller.setPlatform(response.getProductItem().getPlatform());
                        return seller;
                    }
                }
        );
    }

    public LiveData<List<Price>> getProductPriceHistory() {
        return Transformations.map(
                mProductItemResponseMutableLiveData,
                (response) -> response == null? null : response.getPriceHistory()
        );
    }

//    public LiveData<List<String>> getProductImageUrls(){
//        return Transformations.map(mProductItemResponseMutableLiveData,
//                response -> response.getImageUrls());
//    }
}
