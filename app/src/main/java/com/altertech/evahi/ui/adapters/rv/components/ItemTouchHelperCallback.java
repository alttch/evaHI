package com.altertech.evahi.ui.adapters.rv.components;

import android.graphics.Canvas;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.altertech.evahi.ui.adapters.rv.BaseAdapter;
import com.altertech.evahi.ui.holders.view.VHBase;

public class ItemTouchHelperCallback<T extends BaseAdapter.IItem, H extends VHBase, ADAPTER extends BaseAdapter<T, H>> extends ItemTouchHelper.Callback {

    private ADAPTER adapter;

    private ItemTouchHelperCallback() {

    }

    public ItemTouchHelperCallback(
            ADAPTER adapter) {
        this();
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return
                false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return
                false;
    }

    @Override
    public void onChildDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView rw,
            RecyclerView.ViewHolder h, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if ((h.itemView.getTop() + dY) > 0 && ((h.itemView.getTop() + dY) + h.itemView.getHeight()) < rw.getHeight()) {
            super
                    .onChildDraw(c, rw, h, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(
                ItemTouchHelper.UP |
                        ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recycler, @NonNull RecyclerView.ViewHolder vh1, @NonNull RecyclerView.ViewHolder vh2) {
        this.adapter.move(
                vh1.getAdapterPosition(),
                vh2.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}
