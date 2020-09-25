package com.altertech.evahi.helpers;

/**
 * Created by oshevchuk on 26.07.2018
 */
public class IntentHelper {

    public enum REQUEST_CODES {
        BAR_CODE_ACTIVITY(1002),
        SETTINGS_ACTIVITY(1003),
        ABOUT_ACTIVITY(1004),
        PROFILES_ACTIVITY(1005);
        int code;

        REQUEST_CODES(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
