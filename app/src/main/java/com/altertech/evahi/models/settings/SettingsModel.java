package com.altertech.evahi.models.settings;

import android.support.annotation.StringRes;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.core.BaseApplication;
import com.altertech.evahi.utils.StringUtil;

/**
 * Created by oshevchuk on 13.02.2019
 */

public class SettingsModel {

    public class SettingsException extends Exception {

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

    private Boolean isHttps = true;

    private String address;

    private String port;

    private String name;

    private String password;

    public SettingsModel() {
    }

    public SettingsModel(Boolean isHttps, String address, String port, String name, String password) {
        this.isHttps = isHttps;
        this.address = address;
        this.port = port;
        this.name = name;
        this.password = password;
    }

    public void parse(String data) throws SettingsException {
        if (StringUtil.isNotEmpty(data)) {
            String[] parts = data.split("\\|");
            for (String part : parts) {
                if (StringUtil.isNotEmpty(part)) {
                    String[] pair = part.split(":");
                    if (pair.length == 2) {
                        switch (pair[0]) {
                            case "scheme":
                                this.isHttps = pair[1] != null && pair[1].equalsIgnoreCase("https");
                                break;
                            case "address":
                                this.address = pair[1];
                                break;
                            case "port":
                                this.port = pair[1];
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
                this.validS();
            }
            if (AppConfig.AUTHENTICATION) {
                this.validU();
            }
        } else {
            throw new SettingsException(R.string.app_a_settings_exception_invalid_code);
        }
    }

    public SettingsModel validS() throws SettingsException {
        if (StringUtil.isEmpty(this.address)/* || !this.address.matches("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})$")*/) {
            throw new SettingsException(R.string.app_a_settings_exception_invalid_address);
        } else if (!StringUtil.isEmpty(this.port) && (!this.port.matches("^(\\d{0,5})$") || !StringUtil.isInteger(this.port) || Integer.valueOf(this.port) > 65535)) {
            throw new SettingsException(R.string.app_a_settings_exception_invalid_port);
        }
        return this;
    }

    /*public SettingsModel validU() throws SettingsException {
        if (StringUtil.isEmpty(this.name) && StringUtil.isNotEmpty(this.password)) {
            throw new SettingsException(R.string.app_a_settings_exception_invalid_name);
        }
        return this;
    }*/

    public SettingsModel validU() throws SettingsException {
        if (StringUtil.isNotEmpty(this.name) || StringUtil.isNotEmpty(this.password)) {
            if (StringUtil.isEmpty(this.name)) {
                throw new SettingsException(R.string.app_a_settings_exception_invalid_name);
            } else if (StringUtil.isEmpty(this.password)) {
                throw new SettingsException(R.string.app_a_settings_exception_invalid_password);
            }
        }
        return this;
    }

    public void saveS(BaseApplication application) {
        application.useHttps(this.isHttps);
        application.setServerAddress(this.address);
        application.setServerPort(StringUtil.isNotEmpty(this.port) && StringUtil.isInteger(this.port) ? Integer.valueOf(this.port) : null);
    }

    public void saveU(BaseApplication application) {
        application.setUserName(this.name);
        application.setUserPassword(this.password);
    }
}
