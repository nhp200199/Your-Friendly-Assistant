package com.phucnguyen.khoaluantotnghiep.model;

import com.google.gson.annotations.SerializedName;

public class Price {
    @SerializedName("itemId")
    private String mId;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("update")
    private long mDate;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }
}
