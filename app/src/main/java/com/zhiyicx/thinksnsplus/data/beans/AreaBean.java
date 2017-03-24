package com.zhiyicx.thinksnsplus.data.beans;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 地域的id和名字，比如028-成都；用于地域pickerView
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public class AreaBean implements IPickerViewData {
    private String area_id;
    private String title;
    private ArrayList<AreaBean> child;

    public AreaBean() {
    }

    public AreaBean(String area_id, String title, ArrayList<AreaBean> child) {
        this.area_id = area_id;
        this.title = title;
        this.child = child;
    }

    public String getAreaId() {
        return area_id;
    }

    public void setAreaId(String area_id) {
        this.area_id = area_id;
    }

    public void setAreaName(String title) {
        this.title = title;
    }

    public ArrayList<AreaBean> getChild() {
        return child;
    }

    public void setChild(ArrayList<AreaBean> child) {
        this.child = child;
    }

    @Override
    public String getPickerViewText() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AreaBean) {
            AreaBean areaBean = (AreaBean) obj;
            return areaBean.getPickerViewText().equals(title);
        }
        return super.equals(obj);
    }
}
