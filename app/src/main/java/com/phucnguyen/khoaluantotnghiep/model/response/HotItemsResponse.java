package com.phucnguyen.khoaluantotnghiep.model.response;

import com.google.gson.annotations.SerializedName;
import com.phucnguyen.khoaluantotnghiep.model.ProductItem;

import java.util.List;

public class HotItemsResponse {
    private boolean success;
    @SerializedName("data")
    private List<ProductItem> items;
    private Pagination pagination;

    public HotItemsResponse(boolean success, List<ProductItem> items) {
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

    public Pagination getPagination() {
        return pagination;
    }

    public class Pagination{
        private int totalMatch;
        private int limit;
        private int currentPage;
        private int lastPage;

        public int getTotalMatch() {
            return totalMatch;
        }

        public void setTotalMatch(int totalMatch) {
            this.totalMatch = totalMatch;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }
    }
}
