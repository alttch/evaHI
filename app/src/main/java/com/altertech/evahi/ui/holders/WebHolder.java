package com.altertech.evahi.ui.holders;

import android.view.View;
import android.widget.ProgressBar;

import com.altertech.evahi.R;

/**
 * Created by oshevchuk on 12.03.2019
 */
public class WebHolder {

    private View view;

    public WebHolder(View view) {
        this.view = view;

        this.view.findViewById(R.id.ui_f_web_progress_container).setVisibility(View.INVISIBLE);
    }

    public void setProgress(int progress) {
        if (progress < 100) {
            this.view.findViewById(R.id.ui_f_web_progress_container).setVisibility(View.VISIBLE);
        } else {
            this.view.findViewById(R.id.ui_f_web_progress_container).setVisibility(View.INVISIBLE);
        }
        ((ProgressBar) this.view.findViewById(R.id.ui_f_web_progress_p)).setProgress(progress);
    }

    public void setVisibility(boolean state) {
        this.view.findViewById(R.id.ui_f_web_view).setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }


}
