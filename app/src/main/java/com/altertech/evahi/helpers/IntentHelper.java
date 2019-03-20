package com.altertech.evahi.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.altertech.evahi.ui.SettingsActivity;

/**
 * Created by oshevchuk on 26.07.2018
 */
public class IntentHelper {

    public enum REQUEST_CODES {
        BAR_CODE_ACTIVITY(1002),
        SETTINGS_ACTIVITY(1003);
        int code;

        REQUEST_CODES(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }


    public static void showSettingsActivity(Context ctx) {
        ((Activity) ctx).startActivityForResult(new Intent(ctx, SettingsActivity.class), REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
        ((Activity) ctx).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
