package com.altertech.evahi.ui.adapters.rv.holder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.altertech.evahi.ui.holders.view.VHBase;

public class Holder<H extends VHBase> extends RecyclerView.ViewHolder {

    public H
            item;

    public Holder(@NonNull H holder) {
        super(
                holder.v());

        this.item = holder;
    }
}