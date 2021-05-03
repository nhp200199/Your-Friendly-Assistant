package com.phucnguyen.khoaluantotnghiep.utils;

public class FormChecker {
    public static boolean isEmailValid(String email) {
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    public static boolean isPasswordValid(String password) {
        //the password must contains these things:
        //It contains at least 8 characters and at most 20 characters.
        //It contains at least one digit.
        //It contains at least one upper case alphabet.
        //It contains at least one lower case alphabet.
        //It doesn’t contain any white space.
        String passwordRegex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        return password.matches(passwordRegex);
    }

    public static boolean isPhoneValid(String phone) {
        return phone.length() == 10;
    }
}
