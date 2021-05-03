package com.phucnguyen.khoaluantotnghiep.utils;

public class Contants {
    public static final int LOAD_VIEW_TYPE = 1;
    public static final int PRODUCT_VIEW_TYPE = 2;

    public enum LoadingState {
        FIRST_LOADING,
        LOADING,
        SUCCESS_WITH_NO_VALUES,
        SUCCESS,
        FIRST_LOAD_ERROR,
        SUB_LOAD_ERROR,
        ERROR_LOGIN_FAILED_NETWORK_ERROR,
        ERROR_LOGIN_FAILED_AUTHENTICATION_ERROR,
        ERROR_LOGIN_FAILED_AUTHORIZATION_ERROR
    }
}
