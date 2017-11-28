package com.zhiyicx.zhibolibrary.model.entity;

/**
 * Created by zhiyicx on 2016/4/9.
 */
public class ApiArea {
    public String area_id;
    public String title;

    public ApiArea(String area_id, String title) {
        this.area_id = area_id;
        this.title = title;
    }

    public String getPickerViewText() {
        return title;
    }

    public String getAreaId() {
        return area_id;
    }

    @Override
    public String toString() {
        return title;
    }
}