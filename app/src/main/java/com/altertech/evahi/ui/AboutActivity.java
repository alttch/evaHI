package com.altertech.evahi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.altertech.evahi.BuildConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.core.BaseApplication;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.utils.StringUtil;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.findViewById(R.id.title_bar_controls_back_button).setOnClickListener(v -> this.onBackPressed());

        final Config config = BaseApplication.get(this).getServerConfig();

        ((TextView) findViewById(R.id.ui_f_about_line_1)).setText(String.format(getResources().getString(R.string.app_a_about_line_1), BuildConfig.VERSION_NAME));

        TextView ui_f_about_line_2 = findViewById(R.id.ui_f_about_line_2);

        ui_f_about_line_2.setVisibility(config != null ? View.VISIBLE : View.GONE);
        ui_f_about_line_2.setText(config != null ? String.format(getResources().getString(R.string.app_a_about_line_2), String.valueOf(config.getVersion())) : StringUtil.EMPTY_STRING);

    }
}
