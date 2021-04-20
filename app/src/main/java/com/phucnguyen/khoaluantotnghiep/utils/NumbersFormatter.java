package com.phucnguyen.khoaluantotnghiep.utils;

public class NumbersFormatter {
    public static String formatFloatToString(float floatNumber, int floatPoint){
        if(floatNumber == (long) floatNumber)
            return String.format("%d",(long)floatNumber);
        else{
            String string = "%." + floatPoint + "f";
            return String.format(string, floatNumber);
        }
    }
}
