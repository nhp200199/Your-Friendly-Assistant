package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.phucnguyen.khoaluantotnghiep.database.AppDatabase;
import com.phucnguyen.khoaluantotnghiep.database.ProductItemDao;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.List;

public class ProductItemRepo {
    private ProductItemDao mProductItemDao;
    private LiveData<PagedList<ProductItem>> mProductItems;

    public ProductItemRepo(Context context) {
        mProductItemDao = AppDatabase.getInstance(context).getProductItemDao();
        mProductItems = new LivePagedListBuilder<Integer, ProductItem>(
                mProductItemDao.getAllProducts(),
                10
        ).build();
    }

    public LiveData<PagedList<ProductItem>> getProductItems() {
        return mProductItems;
    }
}
