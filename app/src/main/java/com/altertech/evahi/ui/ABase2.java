package com.altertech.evahi.ui;

import android.app.Application;
import android.content.res.Configuration;

import com.altertech.evahi.R;
import com.altertech.evahi.ui.base.ABase;

/**
 * Created by oshevchuk on 11.03.2019
 */
public class ABase2<APP extends Application> extends ABase<APP> {

    protected boolean
            state = false;

    private long
            tPause = 0L;

    @Override
    public void configuration(Configuration config) {
        this
                .getWindow().setBackgroundDrawableResource(config.orientation == Configuration.ORIENTATION_PORTRAIT ? R.drawable.background_custom_1 : R.drawable.background_custom_2);

    }

    @Override
    public void resume() {
        boolean state = this.state;
        if (this.state) {
            this.state = false;
        }
        this.resume(state);
    }

    public void resume(boolean state) {

    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public void pause() {
        this
                .tPause = System.currentTimeMillis();
    }

    public long getTPause() {
        return tPause;
    }
}
