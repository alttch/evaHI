package com.altertech.evahi.ui.adapters.rv;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.altertech.evahi.ui.adapters.list.Selector;
import com.altertech.evahi.ui.adapters.rv.holder.Holder;
import com.altertech.evahi.ui.adapters.rv.i.IBaseAdapter;
import com.altertech.evahi.ui.holders.view.VHBase;
import com.altertech.evahi.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public abstract class BaseAdapter<T extends BaseAdapter.IItem, H extends VHBase> extends RecyclerView.Adapter<Holder<H>> implements IBaseAdapter<T, H> {

    @LayoutRes
    private int
            l;

    protected List<IItem>
            items = new ArrayList<>();

    protected Context
            c;

    public BaseAdapter(
            Context c, int l) {
        this.c = c;
        this.l = l;
        this
                .init(

                );
    }

    public BaseAdapter(
            Context c, int l, List<T> arg1) {
        this(c, l);
        this
                .oAdd(arg1);
    }

    public BaseAdapter(
            Context c, int l, Injection arg1, List<T> arg2) {
        this(c, l);
        this
                .oAdd(arg1)
                .oAdd(arg2);

    }

    @Override
    public void init() {

    }

    /*clean and set*/
    public boolean data(
            List<T> arg1) {

        return this.oClear()
                .oAdd(arg1)
                .uiNotifAll().getItemCount() == 0;
    }

    public boolean data(
            List<T> arg1, Injection arg2) {

        return this.oClear()
                .oAdd(arg1)
                .oAdd(arg2)
                .uiNotifAll().getItemCount() == 0;
    }

    public boolean data(
            Injection arg1, List<T> arg2) {

        return this.oClear()
                .oAdd(arg1)
                .oAdd(arg2)
                .uiNotifAll().getItemCount() == 0;
    }

    public boolean data(
            Injection arg1, List<T> arg2, Injection arg3) {

        return this.oClear()
                .oAdd(arg1)
                .oAdd(arg2)
                .oAdd(arg3)
                .uiNotifAll().getItemCount() == 0;
    }

    public boolean data(
            Injection arg1, List<T> arg2, Injection arg3, List<T> arg4, Injection arg5, List<T> arg6) {

        return this.oClear()
                .oAdd(arg1)
                .oAdd(arg2)
                .oAdd(arg3)
                .oAdd(arg4)
                .oAdd(arg5)
                .oAdd(arg6)
                .uiNotifAll().getItemCount() == 0;
    }

    public boolean data(
            List<T> arg1, Injection arg2, List<T> arg3) {

        return this.oClear()
                .oAdd(arg1)
                .oAdd(arg2)
                .oAdd(arg3)
                .uiNotifAll().getItemCount() == 0;
    }

    /*add*/
    public boolean add(
            List<T> arg1) {
        return this.add(arg1, this.items.size());
    }

    public boolean add(
            List<T> arg1, int position) {
        if (this.items.size() > 0) {
            int p1 = this.items.size() - 1;
            if (this.items.get(p1) instanceof InjectionPagination) {
                this.notifyItemChanged(p1);
            }
        }
        return this.oAdd(arg1, position).uiNotifInsertRange(position, Utils.Lists.safe(arg1).size()).getItemCount() == 0;
    }

    public BaseAdapter<T, H> update(
            int i, T t) {
        this.items.set(i, t);
        return this;
    }

    public BaseAdapter<T, H> remove(
            int i) {
        this.items.remove(i);
        return this;
    }

    @NonNull
    @Override
    public Holder<H> onCreateViewHolder(@NonNull ViewGroup view, int i) {
        return
                new Holder<>((H) new VHBase(LayoutInflater.from(this.c).inflate(i, view, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder view, int position) {
        if (this.items.get(view.getAdapterPosition()) instanceof Injection) {
            this.bind(
                    (H) view.item, (Injection) this.items.get(view.getAdapterPosition()), view.getAdapterPosition());
        } else {
            this.bind(
                    view, (T) this.items.get(view.getAdapterPosition()), view.getAdapterPosition());
        }
    }

    @Override
    public void bind(H holder, BaseAdapter.Injection item, int i) {

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return
                this.items.get(position).inst(Injection.class)
                        ? this.items.get(position).cast(Injection.class).getL()
                        : this.l;
    }

    public IItem first() {
        if (this.items.size() > 0) {
            return
                    this.items.get(0);
        }
        return null;
    }

    public List<T> selected() {
        List<T>
                checked = new ArrayList<>();
        for (IItem item : this.items) {
            if (item instanceof Selector && ((Selector) item).isSelected()) {
                checked.add((T) item);
            }
        }
        return checked;
    }

    public IItem get(int i) {
        return this.items.get(i);
    }

    public BaseAdapter<T, H> select(int index, boolean multiple) {
        return select(index, multiple, true);
    }

    public BaseAdapter<T, H> select(int index, boolean multiple, boolean notif) {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i) instanceof Selector) {
                if (multiple) {
                    if (i == index) {
                        ((Selector) this.items.get(i)).setSelected(!((Selector) this.items.get(i)).isSelected());
                    }
                } else {
                    ((Selector) this.items.get(i)).setSelected(i == index);
                }
            }
        }
        if (notif) {
            this.notifyDataSetChanged();
        }
        return this;
    }

    public int selectedIndex() {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i) instanceof Selector && ((Selector) this.items.get(i)).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public List<T> items() {
        List<T> items = new ArrayList<>(this.items.size());
        for (
                IItem item : this.items) {
            items.add((T) item);
        }
        return items;
    }

    public interface IItem extends Serializable {

        default boolean inst(Class<?> cls) {
            return
                    cls != null && cls.isInstance(this);
        }

        default <T extends IItem> T cast(Class<T> cls) {
            return
                    this.inst(cls) ? cls.cast(this) : null;
        }
    }

    public static class Injection extends Selector implements IItem {

        private @LayoutRes
        int l;

        private int key = -1;

        public Injection(@LayoutRes int l) {
            this.l = l;
        }

        public Injection(@LayoutRes int l, int key) {
            this(l);
            this.key = key;
        }

        public int getL() {
            return l;
        }

        public int getK() {
            return key;
        }
    }

    public static class InjectionPagination extends Injection {

        public InjectionPagination(int l) {
            super(l);
        }
    }

    /*actions*/
    private BaseAdapter<T, H> oClear() {
        if (this.items != null) {
            this.items.clear();
        }
        return this;
    }

    private BaseAdapter<T, H> oAdd(List<T> items) {
        return this.oAdd(items, this.items.size());
    }

    private BaseAdapter<T, H> oAdd(List<T> items, int position) {
        if (items != null) {
            this.items.addAll(position, items);
        }
        return this;
    }

    private BaseAdapter<T, H> oAdd(Injection injection) {
        if (injection != null) {
            this.items.add(injection);
        }
        return this;
    }

    public BaseAdapter<T, H> uiNotifAll() {
        this
                .notifyDataSetChanged();
        return this;
    }

    public BaseAdapter<T, H> uiNotifInsertRange(int start, int count) {
        this
                .notifyItemRangeInserted(start, count);
        return this;
    }

    public BaseAdapter<T, H> remove(T[] items) {
        Iterator<IItem> i = this.items.iterator();
        while (
                i.hasNext()) {

            IItem item = i.next();

            for (
                    T remove : items) {
                if (item.equals(remove)) {
                    i
                            .remove();
                    break;
                }
            }
        }
        return
                this;
    }

    public BaseAdapter<T, H> move(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(this.items, i, i + 1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(this.items, i, i - 1);
            }
        }
        this.notifyItemMoved(from, to);
        return
                this;
    }

    public <O> O and(O o) {
        return o;
    }


}
