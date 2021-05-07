package com.phucnguyen.khoaluantotnghiep.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("_id")
    private String userId;
    private String name;
    private String email;
    @SerializedName("TrackedItemsTiki")
    private List<TrackedItem> trackedTikiProducts;
    @SerializedName("TrackedItemsShopee")
    private List<TrackedItem> trackedShopeeProducts;

    public User(String userId, String name, String email, List<TrackedItem> trackedTikiProducts, List<TrackedItem> trackedShopeeProducts) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.trackedTikiProducts = trackedTikiProducts;
        this.trackedShopeeProducts = trackedShopeeProducts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TrackedItem> getTrackedTikiProducts() {
        return trackedTikiProducts;
    }

    public void setTrackedTikiProducts(List<TrackedItem> trackedTikiProducts) {
        this.trackedTikiProducts = trackedTikiProducts;
    }

    public List<TrackedItem> getTrackedShopeeProducts() {
        return trackedShopeeProducts;
    }

    public void setTrackedShopeeProducts(List<TrackedItem> trackedShopeeProducts) {
        this.trackedShopeeProducts = trackedShopeeProducts;
    }

    public class TrackedItem{
        private long create;
        private ProductItem item;

        public TrackedItem(long create, ProductItem item) {
            this.create = create;
            this.item = item;
        }

        public long getCreate() {
            return create;
        }

        public void setCreate(long create) {
            this.create = create;
        }

        public ProductItem getItem() {
            return item;
        }

        public void setItem(ProductItem item) {
            this.item = item;
        }
    }
}
