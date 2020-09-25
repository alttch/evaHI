package com.altertech.evahi.utils;

/**
 * Created by oshevchuk on 26.07.2018
 */
public class StringUtil {

    public static String EMPTY_STRING = "";

    public static boolean isEmpty(String string) {
        return (string == null || string.length() == 0);
    }

    public static boolean isNotEmpty(String string) {
        return (string != null && string.length() > 0);
    }


}
