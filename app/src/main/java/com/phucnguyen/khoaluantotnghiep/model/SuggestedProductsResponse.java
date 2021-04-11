package com.phucnguyen.khoaluantotnghiep.model;

import java.util.List;

public class SuggestedProductsResponse {
    private boolean success;
    private List<ProductItem> items;

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
}
