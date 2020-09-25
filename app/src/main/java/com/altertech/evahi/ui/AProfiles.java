package com.altertech.evahi.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.altertech.evahi.R;
import com.altertech.evahi.core.BApp;
import com.altertech.evahi.models.profiles.Profile;
import com.altertech.evahi.models.profiles.Profiles;
import com.altertech.evahi.ui.adapters.rv.BaseAdapter;
import com.altertech.evahi.ui.base.ABase;
import com.altertech.evahi.ui.holders.view.VHBase;

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

        this.h.click(R.id.title_bar_controls_back_button, view -> this.back());

        RecyclerView items = this.findViewById(R.id.items);

        items.setAdapter(this.adapter = new BaseAdapter<Profile, VHBase>(this, R.layout.ui_i_profile, this.profiles.profiles) {
            @Override
            public void bind(VHBase holder, Profile item, int i) {

                holder.text(R.id.name, item.name)
                        .backgroundResource(item.id.equals(app.id()) ? R.drawable.background_profile_item_selector_1 : R.drawable.background_profile_item_selector_2)
                        .click(view -> {
                            app.id(item.id).and(this).uiNotifAll().and(AProfiles.this).result(RESULT_OK);
                        }).clickLong(view -> {

                    return true;
                }).clickable(!item.id.equals(app.id()));
            }
        });
        items.setLayoutManager(new LinearLayoutManager(this));
    }
}