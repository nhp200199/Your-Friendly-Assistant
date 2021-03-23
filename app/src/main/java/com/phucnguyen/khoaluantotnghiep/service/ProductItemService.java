package com.phucnguyen.khoaluantotnghiep.service;

import com.phucnguyen.khoaluantotnghiep.model.ProductItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductItemService {
    @GET(value = "api/v1/items/info")
    Call<ProductItemResponse> getItem(@Query("url") String url,
                                      @Query("include") String include);
}
