package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/24
 * @Contact master.jungle68@gmail.com
 */

public class VerifiedBean extends CacheBean implements Parcelable ,Serializable{
    private static final long serialVersionUID = 8258245331800324562L;
    /**
     * "type" : "user"    user 个人, org 企业
     * "icon" : "http:hahhdgh.jpg"
     */

    private String type;
    private String icon;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.icon);
    }

    public VerifiedBean() {
    }

    protected VerifiedBean(Parcel in) {
        this.type = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<VerifiedBean> CREATOR = new Parcelable.Creator<VerifiedBean>() {
        @Override
        public VerifiedBean createFromParcel(Parcel source) {
            return new VerifiedBean(source);
        }

        @Override
        public VerifiedBean[] newArray(int size) {
            return new VerifiedBean[size];
        }
    };

    @Override
    public String toString() {
        return "VerifiedBean{" +
                "type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
