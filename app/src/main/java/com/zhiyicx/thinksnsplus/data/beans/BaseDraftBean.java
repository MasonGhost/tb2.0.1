package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/08/22/10:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BaseDraftBean extends BaseListBean{


    public BaseDraftBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected BaseDraftBean(Parcel in) {
        super(in);
    }

}
