package com.zhiyicx.baseproject.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LiuChao
 * @describe 用于列表中的实体基类，只要来处理maxId
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public class BaseListBean implements Parcelable {

    protected Long maxId;

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }


    public BaseListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.maxId);
    }

    protected BaseListBean(Parcel in) {
        this.maxId = in.readLong();
    }

    public static final Creator<BaseListBean> CREATOR = new Creator<BaseListBean>() {
        @Override
        public BaseListBean createFromParcel(Parcel source) {
            return new BaseListBean(source);
        }

        @Override
        public BaseListBean[] newArray(int size) {
            return new BaseListBean[size];
        }
    };
}
