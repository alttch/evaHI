package com.altertech.evahi.ui.dialog.listeners;

import com.altertech.evahi.ui.dialog.obj.Dialog;

/**
 * Created by oshevchuk on 03.07.2018
 */
public interface DialogCallBackYesCancel<T> {
    void dialogYes(Dialog dialog, T... o);

    void dialogCancel(Dialog dialog);
}
