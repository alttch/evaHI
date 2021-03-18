package com.altertech.evahi.ui.base.i;

import android.content.Intent;
import android.content.res.Configuration;
import androidx.annotation.NonNull;


public interface IACallBack {

    void init();

    void created();

    void resume();

    void pause();

    void back();

    void result(
            int request, int result, Intent data);

    void permissions(
            int request, @NonNull String[] permissions, @NonNull int[] results);

    void configuration(
            Configuration config);
}
