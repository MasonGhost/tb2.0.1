package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/20
 * @Contact master.jungle68@gmail.com
 */

public class IMBean extends CacheBean implements Serializable, Parcelable {
    public static final long serialVersionUID = 5368710083L;
    private int user_id;
    private String im_password;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getIm_password() {
        return im_password;
    }

    public void setIm_password(String im_password) {
        this.im_password = im_password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.im_password);
    }

    public IMBean() {
    }

    protected IMBean(Parcel in) {
        this.user_id = in.readInt();
        this.im_password = in.readString();
    }

    public static final Parcelable.Creator<IMBean> CREATOR = new Parcelable.Creator<IMBean>() {
        @Override
        public IMBean createFromParcel(Parcel source) {
            return new IMBean(source);
        }

        @Override
        public IMBean[] newArray(int size) {
            return new IMBean[size];
        }
    };

    @Override
    public String toString() {
        return "IMBean{" +
                "user_id=" + user_id +
                ", im_password='" + im_password + '\'' +
                '}';
    }
}
