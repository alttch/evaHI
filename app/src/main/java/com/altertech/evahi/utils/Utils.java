package com.altertech.evahi.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Utils {

    private Utils() {
    }

    public static class Views {
        public static boolean isLandscape(Context context) {
            return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        }

        public static void margins(View v, int l, int t, int r, int b) {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        }
    }

    public static class Strings {

        public static final String EMPTY = "";

        public static boolean empty(String string) {
            return (string == null || string.length() == 0);
        }

        public static boolean notEmpty(String string) {
            return (string != null && string.length() > 0);
        }

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

    public static class Cookies {
        public static String get(String in, String name) {
            if (in == null || name == null || name.isEmpty()) {
                return
                        Strings.EMPTY;
            } else {
                String[] cookies = in.split(
                        ";"
                );
                if (
                        cookies.length == 0) {
                    return
                            Strings.EMPTY;
                } else {
                    for (String cookie : cookies) {
                        String[] kv = cookie.trim(

                        ).split("=", 2);
                        if (
                                kv.length == 2 && kv[0].equalsIgnoreCase(name)) {
                            return
                                    kv[1];
                        }
                    }
                    return Strings.EMPTY;
                }
            }
        }

        public static Map<String, String> headers(String name, String password) {

            Map<String, String> headers = new HashMap<>(

            );
            headers.put("Authorization", "Basic " + android.util.Base64.encodeToString(String.format("%s:%s", Strings.val(name), Strings.val(password)).getBytes(), android.util.Base64.NO_WRAP));
            return
                    headers;
        }
    }

    public static class Permissions {

        public static boolean has(
                Context context,
                String permission) {
            return ActivityCompat.checkSelfPermission(
                    context,
                    permission
            ) == PackageManager.PERMISSION_GRANTED;
        }

    }
}
