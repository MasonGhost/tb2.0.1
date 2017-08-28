package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.cache.CacheBean;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public class LocationContainerBean extends CacheBean {

    /**
     * items : [{"id":2508,"name":"成都市","pid":2507,"extends":"","created_at":"2017-06-02 08:44:10","updated_at":"2017-06-02 08:44:10"}]
     * tree : {"id":2507,"name":"四川省","pid":1,"extends":"","created_at":"2017-06-02 08:44:10","updated_at":"2017-06-02 08:44:10","parent":{"id":1,"name":"中国","pid":0,"extends":"3","created_at":"2017-06-02 08:43:54","updated_at":"2017-06-02 08:43:54","parent":null}}
     */

    private LocationBean tree;
    private List<LocationBean> items;

    public LocationBean getTree() {
        return tree;
    }

    public void setTree(LocationBean tree) {
        this.tree = tree;
    }

    public List<LocationBean> getItems() {
        return items;
    }

    public void setItems(List<LocationBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "LocationContainerBean{" +
                "tree=" + tree +
                ", items=" + items +
                '}';
    }
}
