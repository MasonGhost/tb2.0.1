package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/30
 * @Contact master.jungle68@gmail.com
 */

public class ComponentStatusBean implements Parcelable,Serializable {
    private static final long serialVersionUID = 53688200;

    private boolean im;

    public boolean isIm() {
        return im;
    }

    public void setIm(boolean im) {
        this.im = im;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.im ? (byte) 1 : (byte) 0);
    }

    public ComponentStatusBean() {
    }

    protected ComponentStatusBean(Parcel in) {
        this.im = in.readByte() != 0;
    }

    public static final Creator<ComponentStatusBean> CREATOR = new Creator<ComponentStatusBean>() {
        @Override
        public ComponentStatusBean createFromParcel(Parcel source) {
            return new ComponentStatusBean(source);
        }

        @Override
        public ComponentStatusBean[] newArray(int size) {
            return new ComponentStatusBean[size];
        }
    };

    @Override
    public String toString() {
        return "ComponentStatusBean{" +
                "im=" + im +
                '}';
    }
}
