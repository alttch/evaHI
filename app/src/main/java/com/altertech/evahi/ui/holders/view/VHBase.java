package com.altertech.evahi.ui.holders.view;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.altertech.evahi.utils.Utils;

public class VHBase {

    private View
            v;

    public VHBase(
            View v) {
        this.v = v;
    }

    public VHBase click(
            View.OnClickListener l) {
        if (this.v != null) {
            this.v.setOnClickListener(l);
        }
        return this;
    }

    public VHBase click(
            @IdRes int id, View.OnClickListener l) {
        View v = this.v(id);
        if (
                v != null) {
            v.setOnClickListener(l);
        }
        return this;
    }

    public VHBase clickLong(
            View.OnLongClickListener l) {
        if (this.v != null) {
            this.v.setOnLongClickListener(l);
        }
        return this;
    }

    public VHBase clickLong(
            @IdRes int id, View.OnLongClickListener l) {
        View v = this.v(id);
        if (
                v != null) {
            v.setOnLongClickListener(l);
        }
        return this;
    }

    public VHBase clickable(
            boolean state) {
        if (
                this.v != null) {
            this.v.setClickable(state);
        }
        return this;
    }

    public VHBase clickable(
            @IdRes int id, boolean state) {
        View v = this.v(id);
        if (
                v != null) {
            v.setClickable(state);
        }
        return this;
    }

    public VHBase touch(
            @IdRes int id, View.OnTouchListener l) {
        View v = this.v(id);
        if (
                v != null) {
            v.setOnTouchListener(l);
        }
        return this;
    }

    public VHBase checked(
            @IdRes int id, boolean state) {
        CheckBox v = this.v(id, CheckBox.class);
        if (
                v != null) {
            v.setChecked(state);
        }
        return this;
    }

    public boolean checked(
            @IdRes int id) {
        CheckBox v = this.v(id, CheckBox.class);
        if (
                v != null) {
            return v.isChecked();
        }
        return false;
    }


    public String text(
            @IdRes int id) {
        TextView v = this.v(id, TextView.class);
        if (
                v != null) {
            return v.getText().toString();
        } else {
            return Utils.Strings.EMPTY;
        }
    }

    public VHBase text(
            @IdRes int id, String text) {
        TextView v = this.v(id, TextView.class);
        if (
                v != null) {
            v.setText(Utils.Strings.val(text));
        }
        return this;
    }

    public VHBase text(
            @IdRes int id, @StringRes int res) {
        TextView v = this.v(id, TextView.class);
        if (
                v != null) {
            v.setText(res);
        }
        return this;
    }

    public VHBase caps(
            @IdRes int id, boolean caps) {
        TextView v = this.v(id, TextView.class);
        if (
                v != null) {
            v.setAllCaps(caps);
        }
        return this;
    }

    public VHBase visible(
            @IdRes int id, int value) {
        View v = this.v(id);
        if (
                v != null) {
            v.setVisibility(value);
        }
        return this;
    }

    public VHBase enable(
            boolean value) {
        if (
                this.v != null) {
            this.v.setEnabled(value);
        }
        return this;
    }

    public VHBase enable(
            @IdRes int id, boolean value) {
        View v = this.v(id);
        if (
                v != null) {
            v.setEnabled(value);
        }
        return this;
    }

    public VHBase backgroundResource(
            int res) {
        if (
                this.v != null) {
            this.v.setBackgroundResource(res);
        }
        return this;
    }

    public VHBase backgroundResource(
            @IdRes int id, int res) {
        View v = this.v(id);
        if (
                v != null) {
            v.setBackgroundResource(res);
        }
        return this;
    }

    public View v(
    ) {
        return
                this.v;
    }

    public View v(
            @IdRes int id) {
        if (this.v != null) {
            return this.v.findViewById(id);
        }
        return null;
    }

    public <T extends View> T v(
            @IdRes int id, Class<T> cls) {
        View v = this.v(id);
        if (
                v != null && cls != null && cls.isInstance(v)) {
            return cls.cast(v);
        }
        return null;
    }

    public <T> T and(T o) {
        return
                o;
    }


}
