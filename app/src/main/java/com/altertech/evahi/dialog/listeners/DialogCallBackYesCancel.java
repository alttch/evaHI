package com.altertech.evahi.dialog.listeners;

import com.altertech.evahi.dialog.obj.CustomAlertDialog;

/**
 * Created by oshevchuk on 03.07.2018
 */
public interface DialogCallBackYesCancel {
    void dialogYes(CustomAlertDialog dialog, Object o);

    void dialogCancel(CustomAlertDialog dialog);
}
