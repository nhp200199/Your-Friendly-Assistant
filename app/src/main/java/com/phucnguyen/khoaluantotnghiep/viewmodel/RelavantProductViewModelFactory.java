package com.phucnguyen.khoaluantotnghiep.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RelavantProductViewModelFactory implements ViewModelProvider.Factory {
    private String productId;
    private String platform;
    private String categoryId;
    private String name;

    public RelavantProductViewModelFactory(String productId, String platform, String categoryId, String name) {
        this.productId = productId;
        this.platform = platform;
        this.categoryId = categoryId;
        this.name = name;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RelavantProductViewModel.class))
            return (T) new RelavantProductViewModel(productId, categoryId, name, platform);
        throw new IllegalArgumentException("Cant Find View Model Class");
    }
}
