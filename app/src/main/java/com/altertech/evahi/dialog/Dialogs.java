package com.altertech.evahi.dialog;

import android.content.Context;
import android.support.annotation.StringRes;

import com.altertech.evahi.R;
import com.altertech.evahi.dialog.listeners.DialogCallBackYesCancel;
import com.altertech.evahi.dialog.obj.Dialog;
import com.altertech.evahi.models.profiles.Profile;

import java.util.List;

/**
 * Created by oshevchuk on 08.05.2018
 */
public class Dialogs {

    public static Dialog execution(final Context context, @StringRes int m_id) {

        Dialog dialog = new Dialog(context, R.layout.dialog_execution);

        dialog.h
                .text(R.id.dialog_message_id, m_id);

        return dialog.canceled(false).view();

    }

    public static <T> Dialog password(final Context context, DialogCallBackYesCancel<T> listener) {
        final Dialog dialog = new Dialog(context, R.layout.dialog_password).canceled(true);
        dialog.h.click(R.id.a_cancel, v -> listener.dialogCancel(dialog)).click(R.id.a_save, v -> {
            listener.dialogYes(dialog, (T) dialog.h.text(R.id.a_password));
        });
        return dialog.view();
    }

    public static <T> Dialog delete(final Context context, DialogCallBackYesCancel<Profile> listener, Profile... obj) {
        final Dialog dialog = new Dialog(context, R.layout.dialog_delete).canceled(true);
        dialog.h.text(R.id.text, R.string.app_dialog_title_delete_profiles).click(R.id.a_cancel, v -> listener.dialogCancel(dialog)).click(R.id.a_yes, v -> {
            listener
                    .dialogYes(dialog, obj);
        });
        return dialog.view();
    }
}
