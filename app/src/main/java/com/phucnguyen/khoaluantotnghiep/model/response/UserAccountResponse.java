package com.phucnguyen.khoaluantotnghiep.model.response;

import com.phucnguyen.khoaluantotnghiep.model.User;

public class UserAccountResponse {
    private boolean success;
    private User user;

    public UserAccountResponse(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
