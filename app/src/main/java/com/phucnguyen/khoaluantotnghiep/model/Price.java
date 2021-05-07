package com.phucnguyen.khoaluantotnghiep.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Price {
    @SerializedName("itemId")
    private String mId;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("update")
    private long mDate;
    private List<DailyPrice> priceChangeInDay;

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

    public List<DailyPrice> getPriceChangeInDay() {
        return priceChangeInDay;
    }

    public void setPriceChangeInDay(List<DailyPrice> priceChangeInDay) {
        this.priceChangeInDay = priceChangeInDay;
    }

    public static class DailyPrice {
        @SerializedName("price")
        private int mPrice;
        @SerializedName("update")
        private long mDate;
        private boolean isFlashSale;

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

        public boolean isFlashSale() {
            return isFlashSale;
        }

        public void setFlashSale(boolean flashSale) {
            isFlashSale = flashSale;
        }
    }
}
