package com.phucnguyen.khoaluantotnghiep.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.phucnguyen.khoaluantotnghiep.model.User;
import com.phucnguyen.khoaluantotnghiep.model.response.UserAccountResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;
import static com.phucnguyen.khoaluantotnghiep.utils.Contants.UserLoadingState.*;

public class UserRepo {
    private MutableLiveData<User> user = new MutableLiveData<User>();
    private MutableLiveData<UserLoadingState> userLoadingState = new MutableLiveData<UserLoadingState>();
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
        userLoadingState.postValue(LOADING);
        service.getUserAccount("Bearer " + tokenId)
                .enqueue(new Callback<UserAccountResponse>() {
                    @Override
                    public void onResponse(Call<UserAccountResponse> call, Response<UserAccountResponse> response) {
                        UserAccountResponse userAccountResponse = response.body();
                        if (userAccountResponse != null) {
                            userLoadingState.postValue(SUCCESS);
                            user.postValue(response.body().getUser());
                        } else {
                            userLoadingState.setValue(INVALID_CREDENTIALS);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAccountResponse> call, Throwable t) {
                        userLoadingState.postValue(NETWORK_ERROR);
                        user.postValue(null);
                    }
                });
        return user;
    }

    public MutableLiveData<UserLoadingState> getUserLoadingState() {
        return userLoadingState;
    }

    public UserService getService() {
        return service;
    }
}
