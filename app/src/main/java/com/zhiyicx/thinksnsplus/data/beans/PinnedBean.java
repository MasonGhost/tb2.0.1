package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PinnedBean implements Parcelable, Serializable {

    public static final int TOP_REFUSE = 2;
    public static final int TOP_REVIEWING = 0;
    public static final int TOP_SUCCESS = 1;

    private static final long serialVersionUID = 8049105405894380204L;
    /**
     * id : 13
     * created_at : 2017-07-27 09:10:04
     * updated_at : 2017-07-27 09:10:04 // 当state为1 或 2时，此项为审核者操作时间
     * channel : news
     * state : 1 // 审核是否通过 1: 通过, 0: 待审核, 2: 被拒绝
     * raw : 0
     * target : 1
     * user_id : 2
     * target_user : 0
     * amount : 50
     * day : 5
     * cate_id : 1
     * expires_at : 2017-07-25 00:00:00 // 当state为1是此项为置顶过期时间
     */

    private int id;
    private String created_at;
    private String updated_at;
    private String channel;
    private int state;
    private int raw;
    private int target;
    private int user_id;
    private int target_user;
    private int amount;
    private int day;
    private int cate_id;
    private String expires_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTarget_user() {
        return target_user;
    }

    public void setTarget_user(int target_user) {
        this.target_user = target_user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.channel);
        dest.writeInt(this.state);
        dest.writeInt(this.raw);
        dest.writeInt(this.target);
        dest.writeInt(this.user_id);
        dest.writeInt(this.target_user);
        dest.writeInt(this.amount);
        dest.writeInt(this.day);
        dest.writeInt(this.cate_id);
        dest.writeString(this.expires_at);
    }

    public PinnedBean() {
    }

    protected PinnedBean(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.channel = in.readString();
        this.state = in.readInt();
        this.raw = in.readInt();
        this.target = in.readInt();
        this.user_id = in.readInt();
        this.target_user = in.readInt();
        this.amount = in.readInt();
        this.day = in.readInt();
        this.cate_id = in.readInt();
        this.expires_at = in.readString();
    }

    public static final Creator<PinnedBean> CREATOR = new Creator<PinnedBean>() {
        @Override
        public PinnedBean createFromParcel(Parcel source) {
            return new PinnedBean(source);
        }

        @Override
        public PinnedBean[] newArray(int size) {
            return new PinnedBean[size];
        }
    };
}