package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */

public class JpushMessageBean implements Parcelable {

    private String message;
    private String type;
    private String action;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "JpushMessageBean{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.type);
        dest.writeString(this.action);
    }

    public JpushMessageBean() {
    }

    protected JpushMessageBean(Parcel in) {
        this.message = in.readString();
        this.type = in.readString();
        this.action = in.readString();
    }

    public static final Parcelable.Creator<JpushMessageBean> CREATOR = new Parcelable.Creator<JpushMessageBean>() {
        @Override
        public JpushMessageBean createFromParcel(Parcel source) {
            return new JpushMessageBean(source);
        }

        @Override
        public JpushMessageBean[] newArray(int size) {
            return new JpushMessageBean[size];
        }
    };
}
