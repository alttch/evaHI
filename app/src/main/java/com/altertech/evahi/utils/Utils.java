package com.altertech.evahi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Utils {
    private Utils() {
    }

    public static Map<String, String> addBasicAuth(String user, String password) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", user, password).getBytes(), Base64.NO_WRAP));
        return hashMap;
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static class Strings {

        public static final String EMPTY = "";

        public static String val(
                String str) {
            return val(str, EMPTY);
        }

        public static String val(
                String str, String def) {
            return
                    str == null ? (def == null ? EMPTY : def) : str;
        }
    }

    public static class Lists {
        public static <T> java.util.List<T> safe(java.util.List<T> other) {
            return
                    other == null ? new ArrayList<>() : other;
        }
    }
}
