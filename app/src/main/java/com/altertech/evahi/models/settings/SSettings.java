package com.altertech.evahi.models.settings;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;

import java.io.Serializable;

/**
 * Created by oshevchuk on 13.02.2019
 */

public class SSettings implements Serializable {

    public static class SettingsException extends Exception {

        @StringRes
        private int message;

        SettingsException(@StringRes int message) {
            this.message = message;
        }

        @StringRes
        public int getCustomMessage() {
            return message;
        }
    }

    private Boolean
            https = true;

    private int
            port = 443;

    private String
            address,
            name,
            password;


    public SSettings() {
    }

    public SSettings(Boolean https, String address, int port, String name, String password) {
        this.https
                = https;
        this.address
                = address;
        this.port
                = port;
        this.name
                = name;
        this.password
                = password;
    }

    public SSettings parse(String data) throws SettingsException {
        if (data != null) {
            String[] parts = data.split("\\|");
            for (String part : parts) {
                if (part != null) {
                    String[] pair = part.split(":");
                    if (pair.length == 2) {
                        switch (pair[0]) {
                            case "scheme":
                                this.https = pair[1] != null && pair[1].equalsIgnoreCase("https");
                                break;
                            case "address":
                                this.address = pair[1];
                                break;
                            case "port":
                                this.port = Integer.parseInt(pair[1]);
                                break;
                            case "user":
                                this.name = pair[1];
                                break;
                            case "password":
                                this.password = pair[1];
                                break;
                        }
                    }
                }
            }
            if (AppConfig.CONFIG == null || !AppConfig.CONFIG.isEnabled()) {
                this
                        .validSSettings();
            }
            if (AppConfig.AUTHENTICATION) {
                this
                        .validASettings();
            }
            return this;
        } else {
            throw new SettingsException(R.string.app_a_settings_exception_invalid_code);
        }
    }

    public Boolean https() {
        return AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? AppConfig.CONFIG.useHttps() : this.https;
    }

    public SSettings https(boolean value) {
        this.https = value;
        return
                this;
    }

    public int port() {
        return AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? AppConfig.CONFIG.getPort() : this.port;
    }

    public SSettings port(int value) {
        this.port = value;
        return
                this;
    }

    public String address() {
        return AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? AppConfig.CONFIG.getAddress() : (this.address != null && !this.address.isEmpty() ? this.address : AppConfig.CONFIG.getAddress());
    }

    public SSettings address(String value) {
        this.address = value;
        return
                this;
    }

    public String name() {
        return this.name != null ? this.name : "";
    }

    public SSettings name(String value) {
        this.name = value;
        return
                this;
    }

    public String password() {
        return this.password != null ? this.password : "";
    }

    public SSettings password(String value) {
        this.password = value;
        return
                this;
    }

    public SSettings validSSettings() throws SettingsException {
        if (this.address == null || this.address.isEmpty()) {
            throw
                    new SettingsException(
                            R.string.app_a_settings_exception_invalid_address);
        } else if (this.port > 65535) {
            throw
                    new SettingsException(
                            R.string.app_a_settings_exception_invalid_port);
        }
        return this;
    }

    public SSettings validASettings() throws SettingsException {
        if ((this.name != null && !this.name.isEmpty()) || (this.password != null && !this.password.isEmpty())) {
            if (
                    this.name == null || this.name.isEmpty()) {
                throw
                        new SettingsException(R.string.app_a_settings_exception_invalid_name);
            } else if (this.password == null || this.password.isEmpty()) {
                throw
                        new SettingsException(R.string.app_a_settings_exception_invalid_password);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "SSettings{" +
                "https=" + https +
                ", port=" + port +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
