package com.altertech.evahi.core.connection.client;

import com.altertech.evahi.core.App;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ApiClient {

    private static int
            cTTL = 15,
            rTTL = 15,
            wTTL = 15;

    private static volatile OkHttpClient
            instance;

    public static void cTTL(
            int cTTL) {
        ApiClient.cTTL = cTTL;
    }

    public static void trTTL(
            int rTTL) {
        ApiClient.rTTL = rTTL;
    }

    public static void wTTL(
            int wTTL) {
        ApiClient.wTTL = wTTL;
    }

    public static <APP extends App> OkHttpClient getInstance(APP application) {

        if (instance == null)
            synchronized (OkHttpClient.class) {
                if (instance == null) {
                    instance = new OkHttpClient.Builder().connectTimeout(ApiClient.cTTL, TimeUnit.SECONDS).readTimeout(ApiClient.rTTL, TimeUnit.SECONDS).writeTimeout(ApiClient.wTTL, TimeUnit.SECONDS).build();
                }
            }
        return instance;
    }
}