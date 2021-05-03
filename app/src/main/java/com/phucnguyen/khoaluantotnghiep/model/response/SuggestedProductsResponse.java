package com.phucnguyen.khoaluantotnghiep.model.response;

import com.phucnguyen.khoaluantotnghiep.model.Category;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.List;

public class SuggestedProductsResponse {
    private boolean success;
    private List<ProductItem> items;
    private List<Category> categories;

    public SuggestedProductsResponse(boolean success, List<ProductItem> items) {
        this.success = success;
        this.items = items;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ProductItem> getItems() {
        return items;
    }

    public void setItems(List<ProductItem> items) {
        this.items = items;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
