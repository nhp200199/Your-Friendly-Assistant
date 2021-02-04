package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.phucnguyen.khoaluantotnghiep.database.AppDatabase;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.List;

public class ProductItemRepo {
    private ProductItemDao mProductItemDao;
    private LiveData<List<ProductItem>> mProductItems;

    public ProductItemRepo(Context context) {
        mProductItemDao = AppDatabase.getInstance(context).getProductItemDao();
        mProductItems = mProductItemDao.getAllProducts();
    }

    public LiveData<List<ProductItem>> getProductItems() {
        return mProductItems;
    }
}
