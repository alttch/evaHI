package com.altertech.evahi.ui.holders;

import android.view.View;
import android.widget.ProgressBar;

import com.altertech.evahi.R;
import com.altertech.evahi.ui.controls.Web;

/**
 * Created by oshevchuk on 12.03.2019
 */
public class HWeb {

    private final View
            view;

    private final Web
            web;

    private final ProgressBar progress;

    public HWeb(View view) {

        this.web = (this.view = view).findViewById(
                R.id.web
        );
        this.progress = this.view.findViewById(R.id.progress);
    }

    public void progress(int progress) {

        this.progress.setProgress(
                progress
        );
        this.progress.setVisibility(
                progress < 100 ? View.VISIBLE : View.INVISIBLE
        );
    }

    public void visibility(
            boolean state
    ) {
        this.web.setVisibility(
                state ? View.VISIBLE : View.INVISIBLE
        );
    }

    public Web getWeb() {
        return
                this.web;
    }
}