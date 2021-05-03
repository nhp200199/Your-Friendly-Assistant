package com.phucnguyen.khoaluantotnghiep.model.response;

import com.phucnguyen.khoaluantotnghiep.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewResponse {
    private int totalReview;
    private List<Review> reviews;
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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
