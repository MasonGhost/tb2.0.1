package com.zhiyicx.baseproject.base;

import com.google.gson.annotations.SerializedName;

/**
 * @author LiuChao
 * @describe 用于列表中的实体基类，只要来处理maxId
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public class BaseListBean {
    @SerializedName("id")
    protected int maxId;

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }
}
