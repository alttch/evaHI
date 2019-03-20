package com.altertech.evahi.core.config;

import com.altertech.evahi.utils.StringUtil;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class Menu {
    private String name, icon, url;

    public String getName() {
        return name != null ? name : StringUtil.EMPTY_STRING;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url != null ? url : "/";
    }

    public void setUrl(String url) {
        this.url = url;
    }
}