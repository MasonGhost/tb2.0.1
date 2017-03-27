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
    private Long created_at;// 用户权限token开始时间(单位秒的时间戳)
    private int expires;// 用户token的有效期(单位:秒)
    private String token;
    private String refresh_token;

    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
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


    public AuthBean() {
    }

    @Override
    public String toString() {
        return "AuthBean{" +
                "created_at=" + created_at +
                ", expires=" + expires +
                ", token='" + token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.created_at);
        dest.writeInt(this.expires);
        dest.writeString(this.token);
        dest.writeString(this.refresh_token);
        dest.writeInt(this.user_id);
    }

    protected AuthBean(Parcel in) {
        this.created_at = (Long) in.readValue(Long.class.getClassLoader());
        this.expires = in.readInt();
        this.token = in.readString();
        this.refresh_token = in.readString();
        this.user_id = in.readInt();
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
