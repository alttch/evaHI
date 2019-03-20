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

    public static String arrayAsString(byte[] bytes) {
        if (bytes != null) {
            StringBuilder temp = new StringBuilder(EMPTY_STRING);
            for (byte b : bytes) {
                temp.append("[").append(b).append("]");
            }
            return temp.toString();
        } else {
            return "NULL";
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
