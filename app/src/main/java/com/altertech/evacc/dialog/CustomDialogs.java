package com.altertech.evacc.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

import com.altertech.evacc.BuildConfig;
import com.altertech.evacc.R;
import com.altertech.evacc.core.BaseApplication;
import com.altertech.evacc.core.config.Config;
import com.altertech.evacc.dialog.obj.CustomAlertDialog;
import com.altertech.evacc.ui.holders.MenuHolder;
import com.altertech.evacc.utils.StringUtil;

/**
 * Created by oshevchuk on 08.05.2018
 */
public class CustomDialogs {

    public static CustomAlertDialog showExecutionDialog(final Context context, @StringRes int t_id, @StringRes int m_id) {
        return new CustomAlertDialog(context, R.layout.dialog_execution)/*.setText(R.id.dialog_title_id, t_id)*/.setText(R.id.dialog_message_id, m_id).canceled(false).mShow();
    }

    /*public static void showMenuDialog(final Context context, MenuHolder.CallBack listener) {
        final CustomAlertDialog dialog = new CustomAlertDialog(context, R.layout.ui_f_menu);
        //new MenuHolder(context, dialog, listener);
        dialog.mShow();
    }

    public static void showAboutDialog(final Context context) {
        final Config config = BaseApplication.get(context).getServerConfig();

        new CustomAlertDialog(context, R.layout.ui_f_about)
                .setText(R.id.ui_f_about_line_1, String.format(context.getResources().getString(R.string.app_a_about_line_1), BuildConfig.VERSION_NAME))
                .setVisible(R.id.ui_f_about_line_2, config != null ? View.VISIBLE : View.GONE)
                .setText(R.id.ui_f_about_line_2, config != null ? String.format(context.getResources().getString(R.string.app_a_about_line_2), String.valueOf(config.getVersion())) : StringUtil.EMPTY_STRING)
                .mShow();
    }*/
}
