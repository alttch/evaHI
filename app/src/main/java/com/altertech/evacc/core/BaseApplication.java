package com.altertech.evacc.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.altertech.evacc.core.config.Config;
import com.altertech.evacc.core.exception.CustomException;
import com.altertech.evacc.core.parser.SingletonMapper;
import com.altertech.evacc.utils.StringUtil;
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
    public void setServerScheme(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_SERVER_SCHEME, value).apply();
    }

    public String getServerScheme() {
        return preferences.getString(KEY_SETTINGS_SERVER_SCHEME, StringUtil.EMPTY_STRING);
    }

    public void setServerPort(int value) {
        this.preferences.edit().putInt(KEY_SETTINGS_SERVER_PORT, value).apply();
    }

    public int getServerPort() {
        return preferences.getInt(KEY_SETTINGS_SERVER_PORT, 80);
    }

    public void setServerAddress(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_SERVER_ADDRESS, value).apply();
    }

    public String getServerAddress() {
        return preferences.getString(KEY_SETTINGS_SERVER_ADDRESS, StringUtil.EMPTY_STRING);
    }

    /*public void setServerPage(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_SERVER_PAGE, value).apply();
    }

    public String getServerPage() {
        return preferences.getString(KEY_SETTINGS_SERVER_PAGE, "/");
    }

    public void setServerLandscapePage(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_SERVER_LANDSCAPE_PAGE, value).apply();
    }

    public String getServerLandscapePage() {
        return preferences.getString(KEY_SETTINGS_SERVER_LANDSCAPE_PAGE, "/");
    }*/

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

    public void setUserKey(String value) {
        this.preferences.edit().putString(KEY_SETTINGS_USER_KEY, value).apply();
    }

    public String getUserKey() {
        return preferences.getString(KEY_SETTINGS_USER_KEY, StringUtil.EMPTY_STRING);
    }

    public boolean isEmptyUser() {
        String user = this.getUserName();
        return user == null || user.isEmpty();
    }

    public String getServerName() {
        String address = this.getServerAddress();
        return address.endsWith("/") ? address.substring(0, address.length() - 1) : address;
    }

    public String getBaseUrl() {
        return String.format("%s://%s:%s", getServerScheme(), getServerAddress(), getServerPort());
    }

    public String prepareUrl(boolean isLandscape) {
        return String.format("%s://%s:%s", getServerScheme(), getServerAddress(), getServerPort()) + (isLandscape ? getServerLandscapePage() : getServerPage());
    }

}
