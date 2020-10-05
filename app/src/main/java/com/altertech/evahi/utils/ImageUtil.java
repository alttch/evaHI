package com.altertech.evahi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static Bitmap convert(String base64Str) {
        byte[] bytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(
                Bitmap.CompressFormat.PNG, 100, stream
        );
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

}