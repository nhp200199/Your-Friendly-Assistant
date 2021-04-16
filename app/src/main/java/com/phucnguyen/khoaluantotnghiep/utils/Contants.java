package com.phucnguyen.khoaluantotnghiep.utils;

public class Contants {
    public static final int LOAD_VIEW_TYPE = 1;
    public static final int PRODUCT_VIEW_TYPE = 2;

    public enum LoadingState{
        FIRST_LOADING,
        LOADING,
        SUCCESS_WITH_NO_VALUES,
        SUCCESS,
        ERROR
    }
}
