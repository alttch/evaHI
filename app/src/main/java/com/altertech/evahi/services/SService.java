package com.altertech.evahi.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.altertech.evahi.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String CHANNEL_ID =
            "notification_8c6188c625ec8a2522390a2ec1fb8373";
    private static int NOTIFICATION_ID =
            322985;

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
            this.getSystemService(
                    NotificationManager.class
            ).createNotificationChannel(new NotificationChannel(CHANNEL_ID, "notification", NotificationManager.IMPORTANCE_LOW));
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
    }

    /*
     * Google Listeners
     *
     * */

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        Log.e("1715", "onConnected");
        LocationServices.FusedLocationApi.requestLocationUpdates(this.client, new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000).setFastestInterval(5000), this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("1715", "onConnectionSuspended = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("1715", "ConnectionResult = " + result);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("1715", "onLocationChanged = " + location);
        this.getSystemService(NotificationManager.class).notify(NOTIFICATION_ID, this.notification(this.getResources().getString(R.string.app_name), "lat:" + location.getLatitude() + ", lng:" + location.getLongitude()));


        new UDP_CLIENT_SENDER("lat:" + location.getLatitude() + ", lng:" + location.getLongitude()).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class UDP_CLIENT_SENDER extends AsyncTask<Void, Void, Void> {

        private String messages = "";

        public UDP_CLIENT_SENDER(String messages) {
            this.messages = messages;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DatagramSocket socket = null;
            try {


                DatagramPacket dp = new DatagramPacket(messages.getBytes(), messages.length(),
                        InetAddress.getByName("10.90.2.124"),
                        4567);
                socket = new DatagramSocket();
                socket.setBroadcast(true);
                socket.send(dp);


            } catch (Exception e) {

            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
            return null;
        }
    }
}
