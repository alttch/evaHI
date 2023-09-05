package com.altertech.evahi.core;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.altertech.evahi.R;
import com.altertech.evahi.core.models.profiles.Profile;
import com.altertech.evahi.core.models.profiles.Profiles;
import com.google.gson.Gson;

/**
 * Created by oshevchuk on 13.02.2019
 */
public class App extends Application {

    public static final String TAG = "tAg1715";

    private transient SharedPreferences
            preferences;

    public static final int RQ_A_BARCODE = 1002;
    public static final int RQ_A_SETTINGS = 1003;
    public static final int RQ_A_ABOUT = 1004;
    public static final int RQ_A_PROFILES = 1005;

    @Override
    public void onCreate() {
        super
                .onCreate();

        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);


        boolean profiles = this.preferences.contains("dc8a93838c343b55e0eeea3cefafb389");
        if (
                !profiles) {
            this
                    .profiles(new Profiles().add(new Profile(this.id, this.getResources().getString(R.string.app_profile_unknown) + "_" + this.id)));
        }

    }

    /*--------------------------------------------------------------------------*/

    private Long
            id = 1L;

    public Long
    id() {
        return this.preferences.getLong("83eadee5ac3b14608986b87aaf075a0b", this.id);
    }

    public App
    id(Long id) {
        this.preferences.edit().putLong("83eadee5ac3b14608986b87aaf075a0b", id).apply();
        return
                this;
    }

    public boolean
    profiles(Profiles profiles) {
        return
                this.preferences.edit().putString("dc8a93838c343b55e0eeea3cefafb389", new Gson().toJson(profiles)).commit();
    }

    public Profiles
    profiles() {
        Profiles profiles = new Gson().fromJson(this.preferences.getString("dc8a93838c343b55e0eeea3cefafb389", null), Profiles.class);
        return
                profiles;
    }


    public boolean
    first() {
        return
                this.preferences.getBoolean("da1fb732393b9862546e8f0cecff0953", true);
    }

    public void
    first(boolean state) {
        this.preferences.edit().putBoolean("da1fb732393b9862546e8f0cecff0953", state).apply();
    }

    public <T> T and(T o) {
        return o;
    }
}
