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

    private String message;  // 基本消息
    private String type; //  type 推送模块类型
    private String action; // action 推送操作类型
    private boolean isNofity; // 是通知还是透传消息，true 代表通知
    private String extras;  //　额外数据

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

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

    public boolean isNofity() {
        return isNofity;
    }

    public void setNofity(boolean nofity) {
        isNofity = nofity;
    }

    public JpushMessageBean() {
    }

    @Override
    public String toString() {
        return "JpushMessageBean{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", isNofity=" + isNofity +
                ", extras='" + extras + '\'' +
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
        dest.writeByte(this.isNofity ? (byte) 1 : (byte) 0);
        dest.writeString(this.extras);
    }

    protected JpushMessageBean(Parcel in) {
        this.message = in.readString();
        this.type = in.readString();
        this.action = in.readString();
        this.isNofity = in.readByte() != 0;
        this.extras = in.readString();
    }

    public static final Creator<JpushMessageBean> CREATOR = new Creator<JpushMessageBean>() {
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
