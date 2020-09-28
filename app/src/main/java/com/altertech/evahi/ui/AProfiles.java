package com.altertech.evahi.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.altertech.evahi.R;
import com.altertech.evahi.core.BApp;
import com.altertech.evahi.dialog.Dialogs;
import com.altertech.evahi.dialog.listeners.DialogCallBackYesCancel;
import com.altertech.evahi.dialog.obj.Dialog;
import com.altertech.evahi.models.profiles.Profile;
import com.altertech.evahi.models.profiles.Profiles;
import com.altertech.evahi.ui.adapters.rv.BaseAdapter;
import com.altertech.evahi.ui.adapters.rv.components.ItemTouchHelperCallback;
import com.altertech.evahi.ui.adapters.rv.holder.Holder;
import com.altertech.evahi.ui.base.ABase;
import com.altertech.evahi.ui.holders.view.VHBase;

import java.util.List;

public class AProfiles extends ABase<BApp> {

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.a_profiles;
    }

    private Profiles profiles;

    private BaseAdapter<Profile, VHBase> adapter;

    @Override
    public void init() {
        this
                .profiles = this.app.profiles();
    }

    @Override
    public void created() {

        this.h.click(R.id.title_bar_controls_back_button, view -> this.back()).visible(R.id.title_bar_controls_delete, View.GONE).click(R.id.title_bar_controls_delete, view -> {

            Dialogs.delete(this, new DialogCallBackYesCancel<Profile>() {
                @Override
                public void dialogYes(
                        Dialog dialog, Profile... profiles) {
                    dialog
                            .dismiss();

                    AProfiles.this
                            .adapter.remove(profiles).uiNotifAll();
                }

                @Override
                public void dialogCancel(
                        Dialog dialog) {
                    dialog.dismiss();
                }
            }, this.adapter.selected().toArray(new Profile[0]));
        });

        RecyclerView oitems = this.findViewById(
                R.id.items
        );

        oitems.setAdapter(this.adapter = new BaseAdapter<Profile, VHBase>(this, R.layout.ui_i_profile, this.profiles.profiles) {

            ItemTouchHelper touch;

            @Override
            public void init() {
                this.touch = new ItemTouchHelper(new ItemTouchHelperCallback<>(this));
                this.touch.attachToRecyclerView(
                        oitems
                );
            }

            @Override
            public void bind(Holder<VHBase> holder, Profile item, int i) {

                AProfiles.this.h.visible(R.id.title_bar_controls_delete, this.selected().size() > 0 && !AProfiles.this.containId(this.selected(), AProfiles.this.app.id()) ? View.VISIBLE : View.GONE);

                holder.item.text(R.id.name, item.name)
                        .backgroundResource(
                                item.id.equals(app.id())
                                        ? (this.selected().size() > 0 ? R.drawable.background_profile_item_selector_2 : R.drawable.background_profile_item_selector_1)
                                        : R.drawable.background_profile_item_selector_2)
                        .visible(R.id.state, item.isSelected() ? View.VISIBLE : View.GONE)
                        .visible(R.id.drag, this.selected().size() > 0 ? View.VISIBLE : View.GONE)
                        .click(view -> {
                            if (
                                    this.selected().size() > 0) {
                                this.select(
                                        i, true, true);
                            } else {
                                AProfiles.this.app.id(item.id).and(this).uiNotifAll().and(AProfiles.this).result(RESULT_OK);
                            }

                        }).clickLong(view -> {
                    this.select(i, true, true);
                    return true;
                }).touch(R.id.drag, (view, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            touch.startDrag(holder);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            view.performClick();
                            break;
                        }
                    }
                    return true;
                }).clickable(!item.id.equals(app.id()));
            }
        });
        oitems.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean containId(List<Profile> items, Long id) {
        for (Profile item : items) {
            if (
                    item.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void pause() {
        this
                .app.profiles(new Profiles(this.adapter.items()));
    }

    @Override
    public void back() {
        List<Profile> items = this.adapter.selected();
        if (
                items.size() > 0) {
            for (Profile item : items) {
                item
                        .setSelected(false);
            }
            this.adapter.uiNotifAll();
        } else {
            super.back();
        }
    }
}