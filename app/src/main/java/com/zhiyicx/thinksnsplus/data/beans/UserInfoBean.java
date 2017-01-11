package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LiuChao
 * @describe  用户信息的实体类
 * @date 2017/1/11
 * @contact email:450127106@qq.com
 */

public class UserInfoBean implements Parcelable {
    String userName;
    String userId;
    String userIcon;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userId);
        dest.writeString(this.userIcon);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.userName = in.readString();
        this.userId = in.readString();
        this.userIcon = in.readString();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
