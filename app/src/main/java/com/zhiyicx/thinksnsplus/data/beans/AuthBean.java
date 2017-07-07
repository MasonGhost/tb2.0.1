package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import java.io.Serializable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */
public class AuthBean extends CacheBean implements Parcelable, Serializable {
    public static final long serialVersionUID = 536871008l;


    private String token;
    private String refresh_token;
    private int user_id;
    private int expires;// 用户token的有效期(单位:秒)
    private int state;
    private int id;
    private String created_at;// 用户权限token开始时间(单位秒的时间戳)
    private String updated_at;// 用户权限token开始时间(单位秒的时间戳)

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public AuthBean() {
    }

    @Override
    public String toString() {
        return "AuthBean{" +
                "token='" + token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", user_id=" + user_id +
                ", expires=" + expires +
                ", state=" + state +
                ", id=" + id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.refresh_token);
        dest.writeInt(this.user_id);
        dest.writeInt(this.expires);
        dest.writeInt(this.state);
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected AuthBean(Parcel in) {
        this.token = in.readString();
        this.refresh_token = in.readString();
        this.user_id = in.readInt();
        this.expires = in.readInt();
        this.state = in.readInt();
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<AuthBean> CREATOR = new Creator<AuthBean>() {
        @Override
        public AuthBean createFromParcel(Parcel source) {
            return new AuthBean(source);
        }

        @Override
        public AuthBean[] newArray(int size) {
            return new AuthBean[size];
        }
    };
}
