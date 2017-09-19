package com.zhiyicx.zhibosdk.model.entity;

/**
 * Created by zhiyicx on 2016/4/9.
 */
public class ZBApiArea {
    public String area_id;
    public String title;

    public ZBApiArea(String area_id, String title) {
        this.area_id = area_id;
        this.title = title;
    }

    public String getPickerViewText() {
        return title;
    }

    public String getAreaId() {
        return area_id;
    }
}