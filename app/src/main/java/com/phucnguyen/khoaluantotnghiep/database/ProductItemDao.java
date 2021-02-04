package com.phucnguyen.khoaluantotnghiep.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
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
}
