package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/9/7.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZanTag implements Serializable {
    public int type;
    public boolean isOwn;

    public ZanTag(int type, boolean isOwn) {
        this.type = type;
        this.isOwn = isOwn;
    }
}
