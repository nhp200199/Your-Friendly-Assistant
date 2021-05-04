package com.phucnguyen.khoaluantotnghiep.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.service.LocalHostService;
import com.phucnguyen.khoaluantotnghiep.service.ProductItemService;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RelavantProductRepo {
    private ProductItemService service;
    private MutableLiveData<Contants.ItemLoadingState> loadingState = new MutableLiveData<Contants.ItemLoadingState>();
    private MutableLiveData<List<ProductItem>> products = new MutableLiveData<List<ProductItem>>();
    OkHttpClient client = new OkHttpClient.Builder()
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            })
            .build();
    private LocalHostService localhostService = new Retrofit.Builder()
            .baseUrl("http://192.168.0.103:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocalHostService.class);

    public RelavantProductRepo(String id, String name, String categoryId, String platform) {
        service = RetrofitInstance.getProductItemService();

        Map<String, String> queries = new HashMap<String, String>();
        queries.put("id", id);
        queries.put("name", name);
        queries.put("categoryId", categoryId);
        queries.put("platform", platform);
        loadingState.postValue(Contants.ItemLoadingState.LOADING);
        service.getRelavantProducts(queries).enqueue(new Callback<List<ProductItem>>() {
            @Override
            public void onResponse(Call<List<ProductItem>> call, Response<List<ProductItem>> response) {
                if (response.isSuccessful()) {
                    List<ProductItem> productItems = response.body();
                    products.postValue(productItems);
                    if (productItems.size() > 0)
                        loadingState.postValue(Contants.ItemLoadingState.SUCCESS);
                    else loadingState.postValue(Contants.ItemLoadingState.SUCCESS_WITH_NO_VALUES);
                } else {
                    loadingState.postValue(Contants.ItemLoadingState.FIRST_LOAD_ERROR);
                    products.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ProductItem>> call, Throwable t) {
                loadingState.postValue(Contants.ItemLoadingState.FIRST_LOAD_ERROR);
                products.postValue(null);
            }
        });
//        localhostService.getRelavantProducts(queries).enqueue(new Callback<List<ProductItem>>() {
//            @Override
//            public void onResponse(Call<List<ProductItem>> call, Response<List<ProductItem>> response) {
//                if (response.isSuccessful()) {
//                    List<ProductItem> productItems = response.body();
//                    products.postValue(productItems);
//                    if (productItems.size() > 0)
//                        loadingState.postValue(Contants.LoadingState.SUCCESS);
//                    else loadingState.postValue(Contants.LoadingState.SUCCESS_WITH_NO_VALUES);
//                } else {
//                    loadingState.postValue(Contants.LoadingState.FIRST_LOAD_ERROR);
//                    products.postValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ProductItem>> call, Throwable t) {
//                loadingState.postValue(Contants.LoadingState.FIRST_LOAD_ERROR);
//                products.postValue(null);
//            }
//        });
    }

    public LiveData<List<ProductItem>> getProducts() {
        return products;
    }

    public LiveData<Contants.ItemLoadingState> getLoadingState() {
        return loadingState;
    }
}
