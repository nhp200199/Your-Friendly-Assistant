package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.User;
import com.phucnguyen.khoaluantotnghiep.model.response.LogInResponse;
import com.phucnguyen.khoaluantotnghiep.repository.UserRepo;
import com.phucnguyen.khoaluantotnghiep.ui.fragment.RegisterFragment;
import com.phucnguyen.khoaluantotnghiep.utils.DialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;
import static com.phucnguyen.khoaluantotnghiep.utils.Contants.UserLoadingState.*;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<String> tokenIdMLiveData;
    private MutableLiveData<UserLoadingState> userLoadingStateMLiveData;
    private MutableLiveData<UserActionState> userActionStateMLiveData;
    private LiveData<User> userLiveData;
    private UserRepo userRepo;
    private LiveData<String> userName;
    private LiveData<String> userMail;
    private LiveData<List<ProductItem>> trackedProducts;
    SharedPreferences sharedPreferences;
    private final String tokenIdSharePref;
    private final String refreshTokenIdSharePref;

    public UserViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("user", Context.MODE_PRIVATE);
        tokenIdSharePref = sharedPreferences.getString("accessToken", null);
        refreshTokenIdSharePref = sharedPreferences.getString("refreshToken", null);
        userRepo = new UserRepo(application);
        userLoadingStateMLiveData = userRepo.getUserLoadingState();
        userActionStateMLiveData = userRepo.getUserActionState();
        tokenIdMLiveData = new MutableLiveData<String>(tokenIdSharePref);
        userLiveData = Transformations.switchMap(tokenIdMLiveData,
                tokenId -> userRepo.getUserByTokenId(tokenId));
        userName = Transformations.map(userLiveData,
                user -> {
                    if (user != null)
                        return user.getName();
                    else return null;
                });
        userMail = Transformations.map(userLiveData,
                user -> {
                    if (user != null)
                        return user.getEmail();
                    else return null;
                });
        trackedProducts = userRepo.getUserTrackedProducts();
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserMail() {
        return userMail;
    }

    public LiveData<List<ProductItem>> getTrackedProducts() {
        return trackedProducts;
    }

    public LiveData<String> getTokenIdMLiveData() {
        return tokenIdMLiveData;
    }

    public LiveData<UserActionState> getUserActionStateMLiveData() {
        return userActionStateMLiveData;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public LiveData<UserLoadingState> getUserLoadingState() {
        return userLoadingStateMLiveData;
    }

    public LiveData<String> getOrderRequest() {
        return userRepo.getTrackedProductFilter();
    }

    public void setNewTokenId(String tokenId) {
        tokenIdMLiveData.setValue(tokenId);
    }

    public void setUserLoadingState(UserLoadingState loadingState) {
        userLoadingStateMLiveData.setValue(loadingState);
    }

    public void setOrderRequest(String request) {
        userRepo.setTrackedProductFilter(request);
    }

    public void logoutUser() {
        String authString = "Bearer " + sharedPreferences.getString("accessToken", null);
        userRepo.getService().logoutUser(authString)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        sharedPreferences.edit()
                                .putString("accessToken", null)
                                .putString("refreshToken", null)
                                .commit();
                        //NONE indicates that no user is currently logged in
                        userLoadingStateMLiveData.setValue(NONE);
                        userRepo.deleteAllProductsFromDatabase();
                        tokenIdMLiveData.setValue(null);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    public void loginUser(String email, String password) {
        userLoadingStateMLiveData.postValue(LOADING);
        userRepo.getService().loginToUser(email,
                password,
                sharedPreferences.getString("deviceToken", null))
                .enqueue(new Callback<LogInResponse>() {
                    @Override
                    public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                        if (response.isSuccessful()) {
                            sharedPreferences.edit()
                                    .putString("accessToken", response.body().getAccessToken())
                                    .commit();
                            sharedPreferences.edit()
                                    .putString("refreshToken", response.body().getRefreshToken())
                                    .apply();
                            setNewTokenId(response.body().getAccessToken());
                            userLoadingStateMLiveData.setValue(SUCCESS);
                            Log.d("LOGIN: ", "Logged In");
                            Log.d("LOGIN-ACCESS-TOKEN: ", response.body().getAccessToken());
                        } else {
                            String errorJsonString;
                            Gson gson = new Gson();
                            try {
                                errorJsonString = response.errorBody().string();
                                JsonObject jsonObject = gson.fromJson(errorJsonString, JsonObject.class);
                                if (jsonObject.get("message").getAsString().equals("Invalid credentials")) {
                                    userLoadingStateMLiveData.setValue(INVALID_CREDENTIALS);
                                } else if (jsonObject.get("message").getAsString().equals("Please verify your email to continue"))
                                    userLoadingStateMLiveData.setValue(NOT_VERIFIED);
                            } catch (IOException e) {
                                e.printStackTrace();
                                userLoadingStateMLiveData.setValue(NETWORK_ERROR);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LogInResponse> call, Throwable t) {
                        userLoadingStateMLiveData.setValue(NETWORK_ERROR);
                    }
                });
    }

    public void createNewAccessTokenFromRefreshToken() {
        userLoadingStateMLiveData.setValue(LOADING);
        userRepo.getService().createNewAccessToken(sharedPreferences.getString("refreshToken", ""))
                .enqueue(new Callback<LogInResponse>() {
                    @Override
                    public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                        if (response.isSuccessful()) {
                            userLoadingStateMLiveData.setValue(SUCCESS);
                            sharedPreferences.edit()
                                    .putString("accessToken", response.body().getAccessToken())
                                    .apply();
                            setNewTokenId(response.body().getAccessToken());

                            //notify ui to retry proceeding user action
                            if (userActionStateMLiveData.getValue() == UserActionState.NOT_AUTHORIZED) {
                                userActionStateMLiveData.setValue(UserActionState.REGAINED_ACCESS_TOKEN);
                            }
                        } else {
                            userLoadingStateMLiveData.setValue(EXPIRED_TOKEN);

                            userActionStateMLiveData.setValue(UserActionState.EXPIRED_TOKEN);
                            userActionStateMLiveData.setValue(UserActionState.NONE);
                            logoutUser();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogInResponse> call, Throwable t) {
                        userLoadingStateMLiveData.setValue(NETWORK_ERROR);
                        userActionStateMLiveData.setValue(UserActionState.NETWORK_ERROR);
                    }
                });
    }

    public void trackProduct(ProductItem item, int desiredPrice) {
        String tokenId = "Bearer " + tokenIdMLiveData.getValue();
        userRepo.trackProduct(item, desiredPrice, tokenId);
    }

    public void deleteTrackedProduct(String productId, String platform, String authString) {
        userRepo.deleteTrackedProduct(productId, platform, "Bearer " + authString);
    }
}
