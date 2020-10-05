package com.altertech.evahi.ui.holders.view;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.altertech.evahi.R;
import com.altertech.evahi.ui.adapters.rv.BaseAdapter;
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

    public boolean checked(
            @IdRes int id) {
        CheckBox v = this.v(id, CheckBox.class);
        if (
                v != null) {
            return v.isChecked();
        }
        return false;
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
            int value) {
        if (
                this.v != null) {
            this.v.setVisibility(value);
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

    public <ADAPTER extends BaseAdapter> List.RV rv(
            @IdRes int id, boolean fixed, boolean nested, RecyclerView.LayoutManager manager, ADAPTER adapter) {
        List.RV v = v(
                id,
                List.RV.class
        );
        if (v != null) {
            return v.adapter(adapter).fixed(fixed).nestedScrollEnabled(nested).manager(manager);
        }
        return null;
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

    /*----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *
     *
     * messages
     *
     *
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public VHBase snack(
            Messages.Snack.Type type, @StringRes int id, Messages.Snack.Duration duration) {
        return this.snack(
                type, this.v.getResources().getString(id), duration);
    }

    public VHBase snack(
            Messages.Snack.Type type, String message, Messages.Snack.Duration duration) {
        Messages.Snack.snack(
                this.v,
                type,
                Utils.Strings.val(message),
                duration);

        return
                this;
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *
     *
     * extensions
     *
     *
     * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    public static class List {

        public static class RV extends RecyclerView {

            public RV(@NonNull Context context) {
                super(context);
            }

            public RV(@NonNull Context context, @Nullable AttributeSet attrs) {
                super(context, attrs);
            }

            public RV(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
            }

            public <ADAPTER extends BaseAdapter> RV adapter(
                    ADAPTER adapter) {
                this.setAdapter(adapter);
                return this;
            }

            public RV manager(
                    RecyclerView.LayoutManager manager) {
                this.setLayoutManager(manager);
                return this;
            }

            public RV nestedScrollEnabled(
                    boolean state) {
                this.setNestedScrollingEnabled(
                        state
                );
                return this;
            }

            public RV fixed(
                    boolean state) {
                this.setHasFixedSize(state);
                return this;
            }
        }

    }

    public static class Messages {

        public static class Snack {

            public enum Type {
                S(R.color.app_s_green_70),
                E(R.color.app_s_red_70),
                W(R.color.app_s_orange_70);
                @ColorRes
                int
                        color;

                Type(int color) {
                    this.color = color;
                }
            }

            public enum Duration {

                SHORT(Snackbar.LENGTH_SHORT), LONG(Snackbar.LENGTH_LONG), INDEFINITE(Snackbar.LENGTH_INDEFINITE);
                int id;

                Duration(int id) {
                    this.id = id;
                }

                public int getId() {
                    return id;
                }
            }

            public static void snack(View view, Type type, String message, Duration duration) {
                Snackbar snackbar = Snackbar.make(
                        view,
                        message,
                        duration.getId()
                );
                snackbar.getView().setBackgroundResource(
                        type.color
                );
                snackbar.show(

                );
            }

        }

    }
}
