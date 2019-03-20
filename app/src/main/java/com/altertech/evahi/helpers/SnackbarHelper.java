package com.altertech.evahi.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.altertech.evahi.R;


/**
 * Created by oshevchuk on 14.12.2018
 */
public class SnackbarHelper {

    public enum State {
        SUCCESS, ERROR, WARNING
    }

    public enum Duration {
        SHORT(Snackbar.LENGTH_SHORT), LONG(Snackbar.LENGTH_LONG), INDEFINITE(Snackbar.LENGTH_INDEFINITE);
        int id;

        Duration(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static void snack(Context context, State state, @StringRes int id, Duration duration) {
        snack(((Activity) context).findViewById(android.R.id.content), state, context.getResources().getString(id), duration);
    }

    public static void snack(Context context, State state, String m, Duration duration) {
        snack(((Activity) context).findViewById(android.R.id.content), state, m, duration);
    }

    public static void snack(View view, State state, @StringRes int id, Duration duration) {
        snack(view, state, view.getResources().getString(id), duration);
    }

    public static void snack(View view, State state, String m, Duration duration) {
        Snackbar snackbar = Snackbar.make(view, m, duration.getId());
        switch (state) {
            case SUCCESS:
                snackbar.getView().setBackgroundResource(R.color.app_s_green_70);
                break;
            case WARNING:
                snackbar.getView().setBackgroundResource(R.color.app_s_orange_70);
                break;
            case ERROR:
                snackbar.getView().setBackgroundResource(R.color.app_s_red_70);
                break;
        }
        snackbar.show();
    }
}
