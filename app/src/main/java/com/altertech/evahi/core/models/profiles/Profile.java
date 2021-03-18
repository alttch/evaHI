package com.altertech.evahi.core.models.profiles;

import androidx.annotation.NonNull;

import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.core.models.s.SSettings;
import com.altertech.evahi.ui.adapters.list.Selector;
import com.altertech.evahi.ui.adapters.rv.BaseAdapter;

public class Profile extends Selector implements BaseAdapter.IItem {
    public Long
            id;
    public String
            name, totraq;
    public SSettings settings = new SSettings();

    public Config
            config;

    public Profile(
            Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Profile config(Config config) {
        this.config = config;
        return
                this;
    }

    public String getPage() {
        if (this.config != null) {
            return
                    this.config.getIndex();
        } else {
            return "/";
        }
    }

    public String getLandscapePage() {
        if (this.config != null) {
            return
                    this.config.getIndex_landscape();
        } else {
            return this.getPage();
        }
    }

    public String prepareUrl(boolean isLandscape) {
        return
                this.settings.url() + (isLandscape ? this.getLandscapePage() : this.getPage());
    }

    @NonNull
    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", settings=" + settings +
                ", config=" + config +
                '}';
    }
}