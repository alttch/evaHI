package com.altertech.evahi.dialog.obj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.altertech.evahi.R;
import com.altertech.evahi.ui.holders.view.VHBase;

/**
 * Created by oshevchuk on 18.12.2018
 */
public class Dialog extends AlertDialog {

    public VHBase h;

    @SuppressLint("InflateParams")
    public Dialog(@NonNull Context context, @LayoutRes int resource) {
        super(context);
        if (this.getWindow() != null) {
            this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        this.setView((this.h = new VHBase(((Activity) context).getLayoutInflater().inflate(resource, null))).v(), (int) getContext().getResources().getDimension(R.dimen.space_20dp), 0, (int) getContext().getResources().getDimension(R.dimen.space_20dp), 0);
    }

    public Dialog canceled(boolean b) {
        this
                .setCancelable(b);
        return this;
    }

    public Dialog view(

    ) {
        this.show();
        return
                this;
    }
}