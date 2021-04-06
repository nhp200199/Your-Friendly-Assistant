package com.phucnguyen.khoaluantotnghiep.service;

import com.phucnguyen.khoaluantotnghiep.model.ProductItemResponse;
import com.phucnguyen.khoaluantotnghiep.model.ReviewResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ProductItemService {
    @GET(value = "api/v1/items/info")
    Call<ProductItemResponse> getItem(@Query("url") String url,
                                      @Query("include") String include);

    @GET(value = "api/v1/items/review/{productId}")
    Call<ReviewResponse> getReview(@Path("productId") String productId,
                                   @QueryMap Map<String, String> options);
}
