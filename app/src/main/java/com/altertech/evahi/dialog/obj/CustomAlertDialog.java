package com.altertech.evahi.dialog.obj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.altertech.evahi.R;

/**
 * Created by oshevchuk on 18.12.2018
 */
public class CustomAlertDialog extends AlertDialog {

    private View view;

    private Thread thread;

    @SuppressLint("InflateParams")
    public CustomAlertDialog(@NonNull Context context, @LayoutRes int resource) {
        super(context);
        if (this.getWindow() != null) {
            this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        this.view = ((Activity) context).getLayoutInflater().inflate(resource, null);
        this.setView(this.view, (int) getContext().getResources().getDimension(R.dimen.space_20dp), 0, (int) getContext().getResources().getDimension(R.dimen.space_20dp), 0);
    }

    public CustomAlertDialog canceled(boolean b) {
        this.setCancelable(b);
        return this;
    }

    public View getView() {
        return view;
    }

    private View getV(@IdRes int id) {
        return this.view.findViewById(id);
    }

    public TextView getTV(@IdRes int id) {
        return this.view.findViewById(id);
    }

    public ImageView getIV(@IdRes int id) {
        return this.view.findViewById(id);
    }

    public Button getBV(@IdRes int id) {
        return this.view.findViewById(id);
    }

    public CustomAlertDialog setText(@IdRes int id, @StringRes int s) {
        this.setText(id, this.getContext().getResources().getString(s));
        return this;
    }

    public CustomAlertDialog setText(@IdRes int id, String s) {
        this.getTV(id).setText(s);
        return this;
    }

    public CustomAlertDialog setVisible(@IdRes int id, int visible) {
        this.getV(id).setVisibility(visible);
        return this;
    }

    public CustomAlertDialog setClickListener(@IdRes int id1, @IdRes int id2, View.OnClickListener listener) {
        this.getV(id1).findViewById(id2).setOnClickListener(listener);
        return this;
    }

    public CustomAlertDialog setClickListener(@IdRes int id, View.OnClickListener listener) {
        this.getV(id).setOnClickListener(listener);
        return this;
    }

    public CustomAlertDialog setDismissListener(Dialog.OnDismissListener listener) {
        this.setOnDismissListener(listener);
        return this;
    }

    public CustomAlertDialog mShow() {
        this.show();
        if (this.thread != null) {
            this.thread.start();
        }
        return this;
    }

    public CustomAlertDialog setThread(Thread thread) {
        this.thread = thread;
        return this;
    }

    public void mDismiss() {
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
        this.dismiss();
    }
}