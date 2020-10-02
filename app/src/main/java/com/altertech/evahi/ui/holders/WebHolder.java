package com.altertech.evahi.ui.holders;

import android.view.View;
import android.widget.ProgressBar;

import com.altertech.evahi.R;
import com.altertech.evahi.ui.controls.CWebView;

/**
 * Created by oshevchuk on 12.03.2019
 */
public class WebHolder {

    private View
            view;

    private CWebView
            web;

    public WebHolder(
            View view) {
        this.view = view;

        this.web = this.view.findViewById(
                R.id.web
        );

        this.view.findViewById(R.id.progress_container).setVisibility(View.INVISIBLE);
    }

    public void setProgress(int progress) {

        this.view.findViewById(R.id.progress_container).setVisibility(
                progress < 100 ? View.VISIBLE : View.INVISIBLE
        );

        ((ProgressBar) this.view.findViewById(R.id.progress)).setProgress(progress);
    }

    public void visibility(
            boolean state
    ) {
        this.web.setVisibility(
                state ? View.VISIBLE : View.INVISIBLE
        );
    }

    public CWebView getWeb() {
        return
                this.web;
    }
}
