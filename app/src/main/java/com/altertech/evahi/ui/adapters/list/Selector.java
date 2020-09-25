package com.altertech.evahi.ui.adapters.list;

import java.io.Serializable;

public class Selector implements Serializable {

    protected boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
