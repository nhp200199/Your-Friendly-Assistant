package com.phucnguyen.khoaluantotnghiep.model.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

public class HotProductsDataSourceFactory extends DataSource.Factory<Integer, ProductItem> {

    private String platform;
    private String category;
    private MutableLiveData<HotProductsDataSource> mHotProductsDataSourceMutableLiveData;

    public HotProductsDataSourceFactory(String platform, String category) {
        this.platform = platform;
        this.category = category;
        mHotProductsDataSourceMutableLiveData = new MutableLiveData<HotProductsDataSource>();
    }

    public String getPlatform() {
        return platform;
    }

    public String getCategory() {
        return category;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MutableLiveData<HotProductsDataSource> getHotProductsDataSourceMutableLiveData() {
        return mHotProductsDataSourceMutableLiveData;
    }

    @NonNull
    @Override
    public DataSource<Integer, ProductItem> create() {
        HotProductsDataSource dataSource = new HotProductsDataSource(platform, category);
        mHotProductsDataSourceMutableLiveData.postValue(dataSource);
        return dataSource;
    }
}
