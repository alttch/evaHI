package com.altertech.evacc.ui;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import com.altertech.evacc.R;

/**
 * Created by oshevchuk on 11.03.2019
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration config) {
        this.getWindow().setBackgroundDrawableResource(config.orientation == Configuration.ORIENTATION_PORTRAIT ? R.drawable.background_custom_1 : R.drawable.background_custom_2);
        super.onConfigurationChanged(config);
    }

}
