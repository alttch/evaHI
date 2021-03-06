package com.altertech.evahi.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.core.parser.SingletonMapper;
import com.altertech.evahi.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

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
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /*server*/
    public void useHttps(boolean value) {
        this.preferences.edit().putBoolean(KEY_SETTINGS_SERVER_SCHEME, value).apply();
    }

    public Boolean useHttps() {
        return AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? AppConfig.CONFIG.useHttps() : preferences.getBoolean(KEY_SETTINGS_SERVER_SCHEME, AppConfig.CONFIG.useHttps());
    }

    public void setServerPort(Integer value) {
        if (value != null) {
            this.preferences.edit().putInt(KEY_SETTINGS_SERVER_PORT, value).apply();
        } else {
            this.preferences.edit().remove(KEY_SETTINGS_SERVER_PORT).apply();
        }
    }

    public int getServerPort() {
        return AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? AppConfig.CONFIG.getPort() : preferences.getInt(KEY_SETTINGS_SERVER_PORT, AppConfig.CONFIG.getPort());
    }

    public void setServerAddress(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_SERVER_ADDRESS, value).apply();
    }

    public String getServerAddress() {
        return AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? AppConfig.CONFIG.getAddress() : preferences.getString(KEY_SETTINGS_SERVER_ADDRESS, AppConfig.CONFIG.getAddress());
    }

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

    /*user*/
    public void setUserName(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_USER_NAME, value).apply();
    }

    public String getUserName() {
        return preferences.getString(KEY_SETTINGS_USER_NAME, StringUtil.EMPTY_STRING);
    }

    public void setUserPassword(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_USER_PASSWORD, value).apply();
    }

    public String getUserPassword() {
        return preferences.getString(KEY_SETTINGS_USER_PASSWORD, StringUtil.EMPTY_STRING);
    }

    public boolean isEmptyUser() {
        String user = this.getUserName();
        return user == null || user.isEmpty();
    }

    public String getBaseUrl() {
        return String.format("%s://%s:%s", useHttps() ? "https" : "http", this.getServerAddress(), this.getServerPort());
    }

    public String prepareUrl(boolean isLandscape) {
        return this.getBaseUrl() + (isLandscape ? this.getServerLandscapePage() : this.getServerPage());
    }

    public boolean isFirstStart() {
        return preferences.getBoolean(KEY_FIRST_START, true);
    }

    public void setFirstStartState(boolean state) {
        this.preferences.edit().putBoolean(KEY_FIRST_START, state).apply();
    }
}
