package com.altertech.evahi.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import com.altertech.evahi.R;

/**
 * Created by oshevchuk on 11.03.2019
 */
public class BaseActivity extends AppCompatActivity {

    boolean state = false;

    private long tPause = 0L;

    @Override
    public void onConfigurationChanged(Configuration config) {
        this.getWindow().setBackgroundDrawableResource(config.orientation == Configuration.ORIENTATION_PORTRAIT ? R.drawable.background_custom_1 : R.drawable.background_custom_2);
        super.onConfigurationChanged(config);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean state = this.state;
        if (this.state) {
            this.state = false;
        }
        this.resume(state);

    }

    @Override
    protected void onPause() {
        super.onPause();

        this.pause();
    }

    protected void resume(boolean state) {

    }

    public void setState(boolean state) {
        this.state = state;
    }

    protected void pause() {
        this.tPause = System.currentTimeMillis();
    }

    public long getTPause() {
        return tPause;
    }
}
