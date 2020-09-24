package com.altertech.evahi.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.altertech.evahi.R;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.core.parser.SingletonMapper;
import com.altertech.evahi.models.settings.SSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oshevchuk on 13.02.2019
 */
public class BaseApplication extends Application implements AppConstants {

    private SharedPreferences preferences;

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this
                .preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean profiles = this.preferences.contains("dc8a93838c343b55e0eeea3cefafb389");
        if (
                !profiles) {
            this
                    .profiles(new Profiles().add(new Profiles.Profile(this.id, this.getResources().getString(R.string.app_profile_unknown) + "_" + this.id)));
        }
    }

    public static class Profiles {

        public List<Profile> profiles = new ArrayList<>();

        public Profiles add(Profile profile) {
            this.profiles
                    .add(profile);
            return this;
        }

        public Profile get(Long id) {
            for (Profile profile : this.profiles) {
                if (profile.id != null && profile.id.equals(id)) {
                    return
                            profile;
                }
            }
            return null;
        }

        public static class Profile {
            public Long
                    id;
            public String
                    name;
            public SSettings settings = new SSettings();

            public Profile(
                    Long id, String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public String toString() {
                return "Profile{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", settings=" + settings +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Profiles{" +
                    "profiles=" + profiles +
                    '}';
        }
    }

    /*--------------------------------------------------------------------------*/

    private Long
            id = 1L;

    public Long id() {
        return
                this.preferences.getLong("83eadee5ac3b14608986b87aaf075a0b", this.id);
    }

    public BaseApplication id(Long id) {
        this.preferences.edit().putLong("83eadee5ac3b14608986b87aaf075a0b", id).apply();
        return
                this;
    }

    public boolean profiles(Profiles profiles) {
        return
                this.preferences.edit().putString("dc8a93838c343b55e0eeea3cefafb389", new Gson().toJson(profiles)).commit();
    }

    public Profiles profiles() {
        return
                new Gson().fromJson(this.preferences.getString("dc8a93838c343b55e0eeea3cefafb389", null), Profiles.class);
    }

    /*--------------------------------------------------------------------------*/

    public String getServerPage() {
        Config config = getServerConfig();
        if (config != null) {
            return config.getIndex();
        } else {
            return "/";
        }
    }

    public String getServerLandscapePage() {
        Config config = getServerConfig();
        if (config != null) {
            return config.getIndex_landscape();
        } else {
            return this.getServerPage();
        }
    }

    public void setServerConfig(Config config) throws CustomException {
        try {
            this.preferences.edit().putString(KEY_SETTINGS_SERVER_CONFIG, config != null ? SingletonMapper.getInstanceJson().writeValueAsString(config) : null).apply();
        } catch (JsonProcessingException e) {
            throw new CustomException(CustomException.Code.PARSE_ERROR);
        }
    }

    public Config getServerConfig() {
        String config = preferences.getString(KEY_SETTINGS_SERVER_CONFIG, null);
        try {
            return config != null && config.length() > 0 ? SingletonMapper.getInstanceJson().readValue(config, Config.class) : null;
        } catch (IOException e) {
            return null;
        }
    }


    public String getBaseUrl(Profiles.Profile profile) {
        return String.format("%s://%s:%s", profile.settings.https() ? "https" : "http", profile.settings.address(), profile.settings.port());
    }

    public String prepareUrl(Profiles.Profile profile, boolean isLandscape) {
        return this.getBaseUrl(profile) + (isLandscape ? this.getServerLandscapePage() : this.getServerPage());
    }

    public boolean first() {
        return
                this.preferences.getBoolean(KEY_FIRST_START, true);
    }

    public void setFirstStartState(boolean state) {
        this.preferences.edit().putBoolean(KEY_FIRST_START, state).apply();
    }
}
