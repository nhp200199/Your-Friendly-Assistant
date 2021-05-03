package com.phucnguyen.khoaluantotnghiep.service;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface LocalHostService {
    @GET("get-relevant-products")
    Call<List<ProductItem>> getRelavantProducts(@QueryMap Map<String, String> query);
}
