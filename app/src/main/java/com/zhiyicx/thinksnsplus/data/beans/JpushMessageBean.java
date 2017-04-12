package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class JpushMessageBean implements Parcelable {
    @Id
    private long creat_time; // 消息创建时间
    private long user_id; // 操作用户
    private String message;  // 基本消息
    private String type; //  type 推送模块类型
    private String action; // action 推送操作类型
    private boolean isNofity; // 是通知还是透传消息，true 代表通知
    private String extras;  //　额外数据
    private boolean isRead;  // 标记是否已读

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(long creat_time) {
        this.creat_time = creat_time;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

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

    @Override
    public String toString() {
        return "JpushMessageBean{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                ", isNofity=" + isNofity +
                ", extras='" + extras + '\'' +
                ", creat_time=" + creat_time +
                ", isRead=" + isRead +
                '}';
    }

    public boolean getIsNofity() {
        return this.isNofity;
    }

    public void setIsNofity(boolean isNofity) {
        this.isNofity = isNofity;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public JpushMessageBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.creat_time);
        dest.writeLong(this.user_id);
        dest.writeString(this.message);
        dest.writeString(this.type);
        dest.writeString(this.action);
        dest.writeByte(this.isNofity ? (byte) 1 : (byte) 0);
        dest.writeString(this.extras);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
    }

    protected JpushMessageBean(Parcel in) {
        this.creat_time = in.readLong();
        this.user_id = in.readLong();
        this.message = in.readString();
        this.type = in.readString();
        this.action = in.readString();
        this.isNofity = in.readByte() != 0;
        this.extras = in.readString();
        this.isRead = in.readByte() != 0;
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
