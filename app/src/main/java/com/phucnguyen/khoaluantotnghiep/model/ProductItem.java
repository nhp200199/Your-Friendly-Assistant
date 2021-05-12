package com.phucnguyen.khoaluantotnghiep.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "products")
public class ProductItem {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String categoryId;
    private String sellerId;
    private String platform;
    private float rating;
    private String productUrl;
    private String thumbnailUrl;
    private int totalReview;
    @SerializedName("currentPrice")
    private int mProductPrice;
    private int mSellerRate;
    @SerializedName("lastPriceChange")
    @Ignore
    private int priceDifference;
    @Ignore
    private boolean isTracked;

    public ProductItem(String id, String name, String categoryId, String sellerId, String platform, float rating,
                       String productUrl, String thumbnailUrl, int totalReview,
                       int productPrice, int sellerRate) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.rating = rating;
        this.productUrl = productUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.totalReview = totalReview;
        mProductPrice = productPrice;
        mSellerRate = sellerRate;
        this.platform = platform;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public float getRating() {
        return rating;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public int getProductPrice() {
        return mProductPrice;
    }

    public int getSellerRate() {
        return mSellerRate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

    public void setProductPrice(int productPrice) {
        mProductPrice = productPrice;
    }

    public void setSellerRate(int sellerRate) {
        mSellerRate = sellerRate;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPriceDifference() {
        return priceDifference;
    }

    public void setPriceDifference(int priceDifference) {
        this.priceDifference = priceDifference;
    }

    public boolean isTracked() {
        return isTracked;
    }

    public void setTracked(boolean tracked) {
        isTracked = tracked;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        ProductItem comparedItem = (ProductItem) obj;
        return this.name.equals(comparedItem.categoryId);
    }
}
