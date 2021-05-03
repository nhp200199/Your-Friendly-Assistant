package com.phucnguyen.khoaluantotnghiep.model.response;

import com.google.gson.annotations.SerializedName;
import com.phucnguyen.khoaluantotnghiep.model.Price;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;
import com.phucnguyen.khoaluantotnghiep.model.Seller;

import java.util.List;

public class ProductItemResponse {
    @SerializedName("item")
    private ProductItem mProductItem;
    @SerializedName("success")
    private boolean responseStatus;
    @SerializedName("seller")
    private Seller mSeller;
    @SerializedName("prices")
    private List<Price> mPriceHistory;

    public ProductItem getProductItem() {
        return mProductItem;
    }

    public void setProductItem(ProductItem productItem) {
        mProductItem = productItem;
    }

    public boolean isResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(boolean responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Seller getSeller() {
        return mSeller;
    }

    public void setSeller(Seller seller) {
        mSeller = seller;
    }

    public List<Price> getPriceHistory() {
        return mPriceHistory;
    }

    public void setPriceHistory(List<Price> priceHistory) {
        mPriceHistory = priceHistory;
    }
}
