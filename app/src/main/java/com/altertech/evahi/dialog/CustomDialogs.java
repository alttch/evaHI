package com.altertech.evahi.dialog;

import android.content.Context;
import android.support.annotation.StringRes;

import com.altertech.evahi.R;
import com.altertech.evahi.dialog.listeners.DialogCallBackYesCancel;
import com.altertech.evahi.dialog.obj.CustomAlertDialog;

/**
 * Created by oshevchuk on 08.05.2018
 */
public class CustomDialogs {

    public static CustomAlertDialog showExecutionDialog(final Context context, @StringRes int m_id) {
        return new CustomAlertDialog(context, R.layout.dialog_execution).setText(R.id.dialog_message_id, m_id).canceled(false).mShow();
    }

    public static CustomAlertDialog showPasswordDialog(final Context context, DialogCallBackYesCancel listener) {
        final CustomAlertDialog dialog = new CustomAlertDialog(context, R.layout.dialog_password).canceled(true);
        dialog.setClickListener(R.id.a_cancel, v -> listener.dialogCancel(dialog)).setClickListener(R.id.a_save, v -> {
            listener.dialogYes(dialog, dialog.getTV(R.id.a_password).getText().toString());
        });
        return dialog.mShow();
    }
}
