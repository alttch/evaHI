package com.altertech.evahi.ui;

import androidx.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.models.profiles.Profile;
import com.altertech.evahi.core.models.profiles.Profiles;
import com.altertech.evahi.ui.adapters.rv.BaseAdapter;
import com.altertech.evahi.ui.adapters.rv.components.ItemTouchHelperCallback;
import com.altertech.evahi.ui.adapters.rv.holder.Holder;
import com.altertech.evahi.ui.base.ABase;
import com.altertech.evahi.ui.dialog.Dialogs;
import com.altertech.evahi.ui.dialog.listeners.DialogCallBackYesCancel;
import com.altertech.evahi.ui.dialog.obj.Dialog;
import com.altertech.evahi.ui.holders.view.VHBase;
import com.altertech.evahi.utils.Utils;

import java.util.Iterator;
import java.util.List;

public class AProfiles extends ABase<App> {

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
            }, this.profiles(this.adapter.selected(), this.app.id()).toArray(new Profile[0]));
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

                AProfiles.this.h.visible(R.id.title_bar_controls_delete, this.selected().size() > 0 /*&& !AProfiles.this.containId(this.selected(), AProfiles.this.app.id())*/ ? View.VISIBLE : View.GONE);

                holder.item.text(R.id.name, item.name)
                        .backgroundResource(
                                item.id.equals(app.id())
                                        ? (this.selected().size() > 0 ? R.drawable.background_profile_item_selector_2 : R.drawable.background_profile_item_selector_1)
                                        : R.drawable.background_profile_item_selector_2)
                        .visible(R.id.state, item.isSelected() ? View.VISIBLE : View.GONE)
                        .visible(R.id.edit, this.selected().size() > 0 ? View.VISIBLE : View.GONE)
                        .visible(R.id.drag, this.selected().size() > 0 ? View.VISIBLE : View.GONE)
                        .click(R.id.edit, view -> Dialogs.text(view.getContext(), new DialogCallBackYesCancel<String>() {
                            @Override
                            public void dialogYes(
                                    Dialog dialog, String[] o
                            ) {
                                if (o != null && o.length == 1 && o[0] != null && !o[0].isEmpty()) {

                                    AProfiles.this.profiles.get(
                                            item.id
                                    ).name = o[0];
                                    AProfiles.this.app.profiles(
                                            AProfiles.this.profiles
                                    );
                                    dialog.dismiss(

                                    );
                                    AProfiles.this.adapter.notifyDataSetChanged(

                                    );
                                } else {
                                    h.snack(VHBase.Messages.Snack.Type.E, R.string.app_a_settings_exception_empty_field, VHBase.Messages.Snack.Duration.SHORT);
                                }
                            }

                            @Override
                            public void dialogCancel(
                                    Dialog dialog
                            ) {
                                dialog.dismiss();
                            }
                        }, Utils.Strings.val(item.name)))
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

    private List<Profile> profiles(List<Profile> profiles, Long id) {
        if (profiles != null && id != null) {
            Iterator<Profile> i = profiles.iterator();
            while (i.hasNext()) {
                Profile profile = i.next();
                if (
                        profile.id.equals(id)) {
                    i.remove();
                }
            }
        }
        return Utils.Lists.safe(profiles);
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