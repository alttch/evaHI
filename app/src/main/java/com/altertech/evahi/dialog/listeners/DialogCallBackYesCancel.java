package com.altertech.evahi.dialog.listeners;

import com.altertech.evahi.dialog.obj.Dialog;

/**
 * Created by oshevchuk on 03.07.2018
 */
public interface DialogCallBackYesCancel<T> {
    void dialogYes(Dialog dialog, T... o);

    void dialogCancel(Dialog dialog);
}
