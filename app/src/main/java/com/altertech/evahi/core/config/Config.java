package com.altertech.evahi.core.config;

import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.utils.StringUtil;

import java.util.List;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class Config {
    private int serial = 0;
    private String index, index_landscape, home_icon;
    private List<Menu> menu;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int version) {
        this.serial = version;
    }

    public String getIndex() {
        return index != null && index.length() > 0 ? index : "/";
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex_landscape() {
        return index_landscape != null && index_landscape.length() > 0 ? index_landscape : StringUtil.EMPTY_STRING;
    }

    public void setIndex_landscape(String index_landscape) {
        this.index_landscape = index_landscape;
    }

    public boolean hasLandscape() {
        return this.index_landscape != null && this.index_landscape.length() > 0;
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

    public Config valid() throws CustomException {
        if (this.serial == 0) {
            throw new CustomException(CustomException.Code.PARSE_ERROR_INVALID_SERIAL);
        } else if (this.index == null || this.index.length() == 0) {
            throw new CustomException(CustomException.Code.PARSE_ERROR_INVALID_INDEX_PAGE);
        }
        return this;
    }
}