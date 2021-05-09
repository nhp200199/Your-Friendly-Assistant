package com.phucnguyen.khoaluantotnghiep.database;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.List;

@Dao
public interface ProductItemDao {
    @Query("SELECT * FROM products")
    LiveData<List<ProductItem>> getAllProducts();

    @Query("SELECT * FROM products WHERE categoryId = :category")
    LiveData<List<ProductItem>> getAllProductsFromCategory(String category);

    @Insert
    Void insertProduct(ProductItem item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Void insertUserTrackedProducts(List<ProductItem> items);

    @Query("DELETE FROM products")
    void deleteAllProducts();
}
