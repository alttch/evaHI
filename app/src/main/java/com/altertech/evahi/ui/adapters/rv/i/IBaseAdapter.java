package com.altertech.evahi.ui.adapters.rv.i;


import com.altertech.evahi.ui.adapters.rv.BaseAdapter;
import com.altertech.evahi.ui.adapters.rv.holder.Holder;
import com.altertech.evahi.ui.holders.view.VHBase;

public interface IBaseAdapter<T extends BaseAdapter.IItem, H extends VHBase> {

    void init(

    );

    void bind(
            Holder<H> holder, T item, int i);

    void bind(
            H holder, BaseAdapter.Injection item, int i);
}
