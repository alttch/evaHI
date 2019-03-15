package com.altertech.evacc.models.settings;

import android.content.Context;
import android.support.annotation.StringRes;

import com.altertech.evacc.R;
import com.altertech.evacc.core.BaseApplication;
import com.altertech.evacc.utils.StringUtil;

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

    private String scheme;

    private String address;

    private String port;

    private String name;

    private String key;

    public SettingsModel() {
    }

    public SettingsModel(String scheme, String address, String port, String name, String key) {
        this.scheme = scheme;
        this.address = address;
        this.port = port;
        this.name = name;
        this.key = key;
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
                                this.scheme = pair[1];
                                break;
                            case "address":
                                this.address = pair[1];
                                break;
                            case "port":
                                this.port = pair[1];
                                break;
                            case "name":
                                this.name = pair[1];
                                break;
                            case "key":
                                this.key = pair[1];
                                break;
                        }
                    }
                }
            }
            this.valid();
        } else {
            throw new SettingsException(R.string.app_a_settings_exception_bad_code);
        }
    }

    public void valid() throws SettingsException {
        if (StringUtil.isEmpty(this.scheme) || !(this.scheme.equalsIgnoreCase("http") || this.scheme.equalsIgnoreCase("https"))) {
            throw new SettingsException(R.string.app_a_settings_exception_bad_scheme);
        } else if (StringUtil.isEmpty(this.address)/* || !this.address.matches("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})$")*/) {
            throw new SettingsException(R.string.app_a_settings_exception_bad_address);
        } else if (StringUtil.isEmpty(this.port) || !this.port.matches("^(\\d{0,4})$")) {
            throw new SettingsException(R.string.app_a_settings_exception_bad_port);
        } else if (StringUtil.isEmpty(this.name)) {
            throw new SettingsException(R.string.app_a_settings_exception_bad_name);
        } else if (StringUtil.isEmpty(this.key)) {
            throw new SettingsException(R.string.app_a_settings_exception_bad_key);
        }
    }

    public void save(Context context) {
        BaseApplication application = BaseApplication.get(context);
        application.setServerScheme(this.scheme);
        application.setServerAddress(this.address);
        application.setServerPort(Integer.valueOf(this.port));
        application.setUserName(this.name);
        application.setUserKey(this.key);
    }
}
