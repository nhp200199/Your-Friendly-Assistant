package com.phucnguyen.khoaluantotnghiep.service;

import androidx.lifecycle.LiveData;

import com.phucnguyen.khoaluantotnghiep.model.CategoriesResponse;
import com.phucnguyen.khoaluantotnghiep.model.HotItemsResponse;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.ProductItemResponse;
import com.phucnguyen.khoaluantotnghiep.model.ReviewResponse;
import com.phucnguyen.khoaluantotnghiep.model.SuggestedProductsResponse;

import java.util.List;
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

    @GET(value = "api/v1/items/search")
    Call<SuggestedProductsResponse> getSuggestedItems(@Query("q") String queryString,
                                                      @Query("platform") String platform);

    @GET("relevant/test-mongo")
    Call<List<ProductItem>> getRelavantProducts(@QueryMap Map<String, String> query);

    @GET("api/v1/items/most-decreasing-item")
    Call<HotItemsResponse> getHotProducts(@Query("platform") String platform,
                                          @Query("category") String category,
                                          @Query("page") int page);

    @GET("api/v1/categories/standard-categories")
    Call<CategoriesResponse> getAllCategories();
}
