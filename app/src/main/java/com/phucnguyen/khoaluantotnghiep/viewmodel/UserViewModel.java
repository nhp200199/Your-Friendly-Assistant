package com.phucnguyen.khoaluantotnghiep.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.User;
import com.phucnguyen.khoaluantotnghiep.repository.UserRepo;
import com.phucnguyen.khoaluantotnghiep.utils.Contants;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<String> tokenIdMLiveData;
    private LiveData<User> userLiveData;
    private UserRepo userRepo;
    private LiveData<String> userName;
    private LiveData<String> userMail;
    private LiveData<List<ProductItem>> trackedProducts;
    SharedPreferences sharedPreferences;

    public UserViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("user", Context.MODE_PRIVATE);
        String tokenIdSharePref = sharedPreferences.getString("accessToken", null);
        String refreshTokenIdSharePref = sharedPreferences.getString("refreshToken", null);
        userRepo = new UserRepo();

        tokenIdMLiveData = new MutableLiveData<String>(tokenIdSharePref);
        userLiveData = Transformations.switchMap(tokenIdMLiveData,
                tokenId -> userRepo.getUserByTokenId(tokenId, refreshTokenIdSharePref));
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
        trackedProducts = Transformations.map(userLiveData,
                user -> {
                    if (user != null) {
                        ArrayList<ProductItem> combineTrackedProducts = new ArrayList<ProductItem>();
                        for (User.TrackedItem tikiItem : user.getTrackedTikiProducts()) {
                            tikiItem.getItem().setPlatform("tiki");
                            combineTrackedProducts.add(tikiItem.getItem());
                        }
                        for (User.TrackedItem shopeeItem : user.getTrackedShopeeProducts()) {
                            shopeeItem.getItem().setPlatform("shopee");
                            combineTrackedProducts.add(shopeeItem.getItem());
                        }
                        return combineTrackedProducts;
                    } else return null;
                });
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

    public LiveData<Contants.LoadingState> getLoadingState() {
        return userRepo.getLoadingState();
    }

    public void setNewTokenId(String tokenId) {
        tokenIdMLiveData.setValue(tokenId);
    }

    public void logoutUser() {
        sharedPreferences.edit()
                .putString("accessToken", null)
                .putString("refreshToken", null)
                .commit();
        tokenIdMLiveData.setValue(null);
    }
}
