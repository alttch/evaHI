package com.altertech.evahi.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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
