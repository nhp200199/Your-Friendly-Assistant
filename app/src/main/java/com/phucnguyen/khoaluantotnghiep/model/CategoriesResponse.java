package com.phucnguyen.khoaluantotnghiep.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class  CategoriesResponse {
    private boolean success;
    @SerializedName("standardCategories")
    private List<Category> cagories;

    public CategoriesResponse(boolean success, List<Category> cagories) {
        this.success = success;
        this.cagories = cagories;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Category> getCagories() {
        return cagories;
    }

    public void setCagories(List<Category> cagories) {
        this.cagories = cagories;
    }
}
