package com.altertech.evacc.core.config;

import java.util.List;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class Config {
    private int version;
    private String index, index_landscape, home_icon;
    private List<Menu> menu;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getIndex() {
        return index != null && index.length() > 0 ? index : "/";
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex_landscape() {
        return index_landscape != null && index_landscape.length() > 0 ? index_landscape : "/";
    }

    public void setIndex_landscape(String index_landscape) {
        this.index_landscape = index_landscape;
    }

    public String getHome_icon() {
        return home_icon;
    }

    public void setHome_icon(String home_icon) {
        this.home_icon = home_icon;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }
}