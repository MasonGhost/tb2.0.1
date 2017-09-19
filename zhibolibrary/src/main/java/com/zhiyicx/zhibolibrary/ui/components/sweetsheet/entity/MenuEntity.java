package com.zhiyicx.zhibolibrary.ui.components.sweetsheet.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;


/**
 * @author zzz40500
 * @version 1.0
 * @date 2015/8/5.
 * @github: https://github.com/zzz40500
 */

public class MenuEntity implements Serializable {

    public
    String iconId;
    public CharSequence name;
    public Drawable icon;
    public int gold;
    public String gift_code;
    public boolean isChecked;

    @Override
    public String toString() {
        return "MenuEntity{" +
                "iconId='" + iconId + '\'' +
                ", name=" + name +
                ", icon=" + icon +
                ", gold=" + gold +
                ", gift_code='" + gift_code + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public MenuEntity() {
    }

    public MenuEntity(String iconId, CharSequence name, Drawable icon, int gold, String gift_code) {
        this.iconId = iconId;
        this.name = name;
        this.icon = icon;
        this.gold = gold;
        this.gift_code = gift_code;
    }

}
