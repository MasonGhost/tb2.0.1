package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/07/12/17:53
 * @Email Jliuer@aliyun.com
 * @Description V2 的动态点赞
 */
public class DynamicLikeBean implements Parcelable, Serializable {

    private static final long serialVersionUID = 1231L;

    /**
     * id : 2
     * user_id : 1
     * target_user : 1
     * likeable_id : 1
     * likeable_type : feeds
     * created_at : 2017-07-12 08:09:07
     * updated_at : 2017-07-12 08:09:07
     */

    private int id;
    private int user_id;
    private int target_user;
    private int likeable_id;
    private String likeable_type;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLikeable_id() {
        return likeable_id;
    }

    public void setLikeable_id(int likeable_id) {
        this.likeable_id = likeable_id;
    }

    public String getLikeable_type() {
        return likeable_type;
    }

    public void setLikeable_type(String likeable_type) {
        this.likeable_type = likeable_type;
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
        dest.writeInt(this.user_id);
        dest.writeInt(this.target_user);
        dest.writeInt(this.likeable_id);
        dest.writeString(this.likeable_type);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public DynamicLikeBean() {
    }

    protected DynamicLikeBean(Parcel in) {
        this.id = in.readInt();
        this.user_id = in.readInt();
        this.target_user = in.readInt();
        this.likeable_id = in.readInt();
        this.likeable_type = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<DynamicLikeBean> CREATOR = new Creator<DynamicLikeBean>() {
        @Override
        public DynamicLikeBean createFromParcel(Parcel source) {
            return new DynamicLikeBean(source);
        }

        @Override
        public DynamicLikeBean[] newArray(int size) {
            return new DynamicLikeBean[size];
        }
    };
}
