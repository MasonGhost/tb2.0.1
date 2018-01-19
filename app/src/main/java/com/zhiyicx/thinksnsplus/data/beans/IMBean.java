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
    /**1.5.0新增 环信登陆的密码  用户名是uid*/
    private String im_pwd_hash;

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

    public String getIm_pwd_hash() {
        return im_pwd_hash;
    }

    public void setIm_pwd_hash(String im_pwd_hash) {
        this.im_pwd_hash = im_pwd_hash;
    }

    public IMBean() {
    }

    @Override
    public String toString() {
        return "IMBean{" +
                "user_id=" + user_id +
                ", im_password='" + im_password + '\'' +
                ", im_pwd_hash='" + im_pwd_hash + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.im_password);
        dest.writeString(this.im_pwd_hash);
    }

    protected IMBean(Parcel in) {
        this.user_id = in.readInt();
        this.im_password = in.readString();
        this.im_pwd_hash = in.readString();
    }

    public static final Creator<IMBean> CREATOR = new Creator<IMBean>() {
        @Override
        public IMBean createFromParcel(Parcel source) {
            return new IMBean(source);
        }

        @Override
        public IMBean[] newArray(int size) {
            return new IMBean[size];
        }
    };
}

