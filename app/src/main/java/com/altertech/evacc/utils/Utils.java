package com.altertech.evacc.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;

import com.altertech.evacc.R;

import java.util.HashMap;
import java.util.Map;

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

    public static String prepareTitle(Context context, String server) {
        return context.getString(R.string.app_name) + " " + server;
    }

    private static int getDoubleslash(String url) {
        int doubleslash = url.indexOf("//");
        if (doubleslash == -1)
            doubleslash = 0;
        else
            doubleslash += 2;
        int start = url.indexOf('/', doubleslash);
        start = start >= 0 ? start + 1 : url.length();
        return start;
    }

    public static String getHost(String url) {
        if (url == null || url.length() == 0)
            return "";
        return url.substring(0, getDoubleslash(url));
    }

    public static String getUri(String url) {
        if (url == null || url.length() == 0)
            return "";
        return url.substring(getDoubleslash(url), url.length());
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
