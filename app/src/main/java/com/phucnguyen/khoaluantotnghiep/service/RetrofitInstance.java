package com.phucnguyen.khoaluantotnghiep.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static final String BASE_URL = "https://519bb137df6144dcbeda18e87d53ad8a-0-s-80.vlab.uit.edu.vn/";
    private static Retrofit mRetrofitInstance;

    public static Retrofit getInstance(){
        if(mRetrofitInstance == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    })
                    .build();

            mRetrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return mRetrofitInstance;
    }
    public static ProductItemService getProductItemService(){
        return getInstance().create(ProductItemService.class);
    }
}
