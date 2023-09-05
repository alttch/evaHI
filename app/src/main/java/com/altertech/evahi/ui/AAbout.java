package com.altertech.evahi.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.ui.base.ABase2;
import com.altertech.evahi.utils.ImageUtil;
import com.altertech.evahi.utils.Utils;

import java.util.Calendar;

public class AAbout extends ABase2<App> {

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.a_about;
    }

    @Override
    public void created() {
        this.findViewById(R.id.title_bar_controls_back_button).setOnClickListener(v -> this.onBackPressed());

        final Config config = this.app.profiles().get(this.app.id()).config;

        final View ui_f_about_line_0 = findViewById(R.id.ui_f_about_line_0);
        ui_f_about_line_0.setVisibility(config != null && Utils.Strings.notEmpty(config.getHome_icon()) ? View.VISIBLE : View.GONE);
        final ImageView ui_f_about_icon = ui_f_about_line_0.findViewById(R.id.ui_f_about_icon);
        ui_f_about_icon.setImageBitmap(config != null && Utils.Strings.notEmpty(config.getHome_icon()) ? ImageUtil.convert(config.getHome_icon()) : BitmapFactory.decodeResource(getResources(), R.drawable.drawable_menu_home));

        final TextView l_1 = findViewById(R.id.ui_f_about_line_1);
        l_1.setText(AppConfig.NAME);
        l_1.setVisibility(Utils.Strings.notEmpty(AppConfig.NAME) ? View.VISIBLE : View.GONE);

        final TextView l_2 = findViewById(R.id.ui_f_about_line_2);
        l_2.setText(AppConfig.VERSION);
        l_2.setVisibility(Utils.Strings.notEmpty(AppConfig.VERSION) ? View.VISIBLE : View.GONE);

        final TextView l_3 = findViewById(R.id.ui_f_about_line_3);
        l_3.setText(AppConfig.COPYRIGHT);
        l_3.setVisibility(Utils.Strings.notEmpty(AppConfig.COPYRIGHT) ? View.VISIBLE : View.GONE);

        boolean nExistAppConfig = l_1.getVisibility() == View.GONE && l_2.getVisibility() == View.GONE && l_3.getVisibility() == View.GONE;

        findViewById(R.id.ui_f_about_line_separator_1).setVisibility(nExistAppConfig ? View.GONE : View.VISIBLE);

        ((TextView) findViewById(R.id.ui_f_about_line_4)).setText(String.format(getResources().getString(R.string.app_a_about_line_name), AppConfig.evaHI_Build));

        ((TextView) findViewById(R.id.ui_f_about_line_5)).setText(String.format(getResources().getString(R.string.app_a_about_line_copyright), Calendar.getInstance().get(Calendar.YEAR)));

        final TextView l_6 = findViewById(R.id.ui_f_about_line_6);
        l_6.setText(R.string.app_a_about_line_link);
        l_6.setOnClickListener(v -> this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_a_about_line_link)))));

        ((TextView) findViewById(R.id.ui_f_about_line_7)).setText(R.string.app_a_about_line_license);

        TextView l_8 = findViewById(R.id.ui_f_about_line_8);
        l_8.setVisibility(config != null ? View.VISIBLE : View.GONE);
        l_8.setText(config != null ? String.format(getResources().getString(R.string.app_a_about_line_web_ui), String.valueOf(config.getSerial())) : Utils.Strings.EMPTY);


        View ui_f_about_container = findViewById(R.id.ui_f_about_container);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ui_f_about_container.getLayoutParams();
        params.height = !nExistAppConfig /*|| ui_f_about_line_0.getVisibility() == View.VISIBLE*/ ? (int) getResources().getDimension(R.dimen.space_400dp) : (int) getResources().getDimension(R.dimen.space_300dp);
        ui_f_about_container.setLayoutParams(params);
        ui_f_about_container.findViewById(R.id.ui_f_about_container_background).setBackgroundResource(!nExistAppConfig /*|| ui_f_about_line_0.getVisibility() == View.VISIBLE*/ ? R.drawable.drawable_about_1 : R.drawable.drawable_about_2);

    }
}
