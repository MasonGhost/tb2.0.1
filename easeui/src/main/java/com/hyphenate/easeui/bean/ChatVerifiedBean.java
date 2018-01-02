package com.hyphenate.easeui.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author Catherine
 * @describe
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatVerifiedBean implements Parcelable{

    private String type;
    private String icon;
    private int status;
    private String description;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.icon);
        dest.writeInt(this.status);
        dest.writeString(this.description);
    }

    public ChatVerifiedBean() {
    }

    protected ChatVerifiedBean(Parcel in) {
        this.type = in.readString();
        this.icon = in.readString();
        this.status = in.readInt();
        this.description = in.readString();
    }

    public static final Creator<ChatVerifiedBean> CREATOR = new Creator<ChatVerifiedBean>() {
        @Override
        public ChatVerifiedBean createFromParcel(Parcel source) {
            return new ChatVerifiedBean(source);
        }

        @Override
        public ChatVerifiedBean[] newArray(int size) {
            return new ChatVerifiedBean[size];
        }
    };
}
