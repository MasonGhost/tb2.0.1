package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CircleJoinedBean implements Parcelable, Serializable {
    private static final long serialVersionUID = -2874474992456690897L;

    /**
     * 0 待审核 1已审核 2驳回
     */
    public enum AuditStatus {
        REJECTED(2),
        REVIEWING(0),
        PASS(1);
        public int value;

        AuditStatus(int value) {
            this.value = value;
        }
    }

    /**
     * 是否被拉黑禁用 1-禁用 0-正常
     */
    public enum DisableStatus {
        DISABLE(1),
        NORMAL(0);
        public int value;

        DisableStatus(int value) {
            this.value = value;
        }
    }

    /**
     * id : 2
     * group_id : 3
     * user_id : 18
     * audit : 0
     * role : founder
     * disabled : 0
     * created_at : 2017-11-29 17:08:16
     * updated_at : 2017-11-29 17:08:17
     */

    private int id;
    private int group_id;
    private int user_id;
    private int audit;
    private String role;
    private int disabled;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.group_id);
        dest.writeInt(this.user_id);
        dest.writeInt(this.audit);
        dest.writeString(this.role);
        dest.writeInt(this.disabled);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public CircleJoinedBean() {
    }

    public CircleJoinedBean(String role) {
        this.role = role;
    }

    protected CircleJoinedBean(Parcel in) {
        this.id = in.readInt();
        this.group_id = in.readInt();
        this.user_id = in.readInt();
        this.audit = in.readInt();
        this.role = in.readString();
        this.disabled = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<CircleJoinedBean> CREATOR = new Creator<CircleJoinedBean>() {
        @Override
        public CircleJoinedBean createFromParcel(Parcel source) {
            return new CircleJoinedBean(source);
        }

        @Override
        public CircleJoinedBean[] newArray(int size) {
            return new CircleJoinedBean[size];
        }
    };
}