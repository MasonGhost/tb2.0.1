package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/22
 * @Contact master.jungle68@gmail.com
 */
public class ThridInfoBean implements Parcelable {

    private String provider;
    private String access_token;
    private String name;
    private boolean check;

    public ThridInfoBean(String provider, String accessToken, String name, boolean check) {
        this.provider = provider;
        this.access_token = accessToken;
        this.name = name;
        this.check = check;
    }

    public ThridInfoBean(String provider, String accessToken, String name) {
        this.provider = provider;
        this.access_token = accessToken;
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThridInfoBean() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.provider);
        dest.writeString(this.access_token);
        dest.writeString(this.name);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
    }

    protected ThridInfoBean(Parcel in) {
        this.provider = in.readString();
        this.access_token = in.readString();
        this.name = in.readString();
        this.check = in.readByte() != 0;
    }

    public static final Creator<ThridInfoBean> CREATOR = new Creator<ThridInfoBean>() {
        @Override
        public ThridInfoBean createFromParcel(Parcel source) {
            return new ThridInfoBean(source);
        }

        @Override
        public ThridInfoBean[] newArray(int size) {
            return new ThridInfoBean[size];
        }
    };
}
