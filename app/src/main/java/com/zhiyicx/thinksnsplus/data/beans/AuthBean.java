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
    private long refresh_ttl; // 刷新 token
    private long ttl;// 用户token的有效期(单位:秒)
    private UserInfoBean user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getRefresh_ttl() {
        return refresh_ttl;
    }

    public void setRefresh_ttl(long refresh_ttl) {
        this.refresh_ttl = refresh_ttl;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeLong(this.refresh_ttl);
        dest.writeLong(this.ttl);
        dest.writeParcelable(this.user, flags);
    }

    public AuthBean() {
    }

    protected AuthBean(Parcel in) {
        this.token = in.readString();
        this.refresh_ttl = in.readLong();
        this.ttl = in.readLong();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
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

    @Override
    public String toString() {
        return "AuthBean{" +
                "token='" + token + '\'' +
                ", refresh_ttl=" + refresh_ttl +
                ", ttl=" + ttl +
                ", user=" + user +
                '}';
    }
}
