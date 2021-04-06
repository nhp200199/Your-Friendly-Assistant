package com.phucnguyen.khoaluantotnghiep.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class ReviewViewModelFactory implements ViewModelProvider.Factory {

    private String productId;
    private String platform;
    private String seller;

    public ReviewViewModelFactory(String productId, String platform, String seller) {
        this.productId = productId;
        this.platform = platform;
        this.seller = seller;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReviewViewModel.class))
            return (T) new ReviewViewModel(productId, platform, seller);
        throw new IllegalArgumentException("Cant Find View Model Class");
    }
}
