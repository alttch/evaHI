package com.altertech.evahi.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.CookieManager;

import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.connection.Post;
import com.altertech.evahi.core.connection.client.ApiClient;
import com.altertech.evahi.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String CHANNEL_ID =
            "notification_8c6188c625ec8a2522390a2ec1fb8373";
    private static int NOTIFICATION_ID =
            322985;
    private App
            app;

    private GoogleApiClient
            client;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(
            Intent intent, int flags, int start) {
        Log.e("1715", "onStartCommand");
        return
                START_STICKY;
    }


    @SuppressLint("MissingPermission")
    public void onCreate() {

        this.app = (App) this.getApplication(

        );
        this.initChannels().startForeground(
                NOTIFICATION_ID, this.notification(getResources().getString(R.string.app_name), "")
        );

        this.client = new GoogleApiClient.Builder(
                this
        ).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        this
                .client.connect();

    }

    private SService initChannels() {
        if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "notification", NotificationManager.IMPORTANCE_LOW);
            channel.setShowBadge(
                    false
            );
            this.getSystemService(
                    NotificationManager.class
            ).createNotificationChannel(channel);
        }
        return this;
    }

    private Notification notification(
            String title, String text) {
        return new NotificationCompat.Builder(
                this, CHANNEL_ID
        ).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(text).setPriority(NotificationCompat.PRIORITY_LOW).build();
    }

    @Override
    public void onDestroy() {
        Log.e("1715", "onDestroy");
        if (this.client != null) {
            this.client.disconnect();
        }
        this.getSystemService(
                NotificationManager.class
        ).cancelAll();
    }

    /*
     * Google Listeners
     *
     * */

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                this.client, new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000).setFastestInterval(5000), this
        );
    }

    @Override
    public void onConnectionSuspended(
            int i
    ) {
        Log.e("1715", "onConnectionSuspended = " + i);
    }

    @Override
    public void onConnectionFailed(
            ConnectionResult result
    ) {
        Log.e("1715", "ConnectionResult = " + result);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.getSystemService(
                NotificationManager.class
        ).notify(NOTIFICATION_ID, this.notification(this.getResources().getString(R.string.app_name), "lat:" + location.getLatitude() + ", lng:" + location.getLongitude()));

        new Post(() -> {

            Request.Builder request = new Request.Builder().url(
                    "https://api.totraq.com/location"
            ).post(
                    RequestBody.create("{\"lat\": " + location.getLatitude() + "  ,\"lng\":" + location.getLongitude() + "}", MediaType.parse("application/json; charset=utf-8"))
            ).addHeader("Cookie", "totraq=" + /*this.app.profiles().get(
                    this.app.id()
            ).totraq*/Utils.Cookies.get(CookieManager.getInstance().getCookie("https://api.totraq.com/"), "totraq"));

            try {
                return ApiClient.getInstance((App) getApplication()).newCall(request.build()).execute();
            } catch (IOException e) {
                return new Response.Builder().code(
                        -1
                ).build();
            }
        }).success((code, data) -> Log.d(App.TAG, "success, code = " + code + ", data = " + data)).error(code -> Log.d(App.TAG, "error, code = " + code)).execute();
    }
}
