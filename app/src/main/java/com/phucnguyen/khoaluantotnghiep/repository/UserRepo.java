package com.phucnguyen.khoaluantotnghiep.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.model.User;
import com.phucnguyen.khoaluantotnghiep.model.response.LogInResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.UserAccountResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;

public class UserRepo {
    private MutableLiveData<User> user = new MutableLiveData<User>();
    private MutableLiveData<LoadingState> loadingState = new MutableLiveData<LoadingState>();
    private UserService service;

    public UserRepo() {
        user = new MutableLiveData<User>();
        service = RetrofitInstance.getInstance().create(UserService.class);
    }

    public LiveData<User> getUserByTokenId(String tokenId, String refreshTokenId) {
        if (tokenId == null) {
            user.postValue(null);
            return user;
        }
        loadingState.postValue(LoadingState.LOADING);
        service.getUserAccount("Bearer " + tokenId)
                .enqueue(new Callback<UserAccountResponse>() {
                    @Override
                    public void onResponse(Call<UserAccountResponse> call, Response<UserAccountResponse> response) {
                        UserAccountResponse userAccountResponse = response.body();
                        if (userAccountResponse != null) {
                            loadingState.postValue(LoadingState.SUCCESS);
                            user.postValue(response.body().getUser());
                        } else if (refreshTokenId != null) {
                            Call<UserAccountResponse> retryLoginWithRefreshTokenIdCall = service.getUserAccount("Bearer " + refreshTokenId);
                            retryLoginWithRefreshTokenId(retryLoginWithRefreshTokenIdCall);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAccountResponse> call, Throwable t) {
                        loadingState.postValue(LoadingState.ERROR_LOGIN_FAILED_NETWORK_ERROR);
                        user.postValue(null);
                    }
                });
        return user;
    }

    private void retryLoginWithRefreshTokenId(Call<UserAccountResponse> retryLoginWithRefreshTokenIdCall) {
        retryLoginWithRefreshTokenIdCall.enqueue(new Callback<UserAccountResponse>() {
            @Override
            public void onResponse(Call<UserAccountResponse> call, Response<UserAccountResponse> response) {
                UserAccountResponse userAccountResponse = response.body();
                if (userAccountResponse != null) {
                    loadingState.postValue(LoadingState.SUCCESS);
                    user.postValue(response.body().getUser());
                } else {
                    switch (response.code()) {
                        case 401: //not authorized
                            loadingState.postValue(LoadingState.ERROR_LOGIN_FAILED_AUTHORIZATION_ERROR);
                            user.postValue(null);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserAccountResponse> call, Throwable t) {
                loadingState.postValue(LoadingState.ERROR_LOGIN_FAILED_NETWORK_ERROR);
                user.postValue(null);
            }
        });
    }

    public LiveData<LoadingState> getLoadingState() {
        return loadingState;
    }
}
