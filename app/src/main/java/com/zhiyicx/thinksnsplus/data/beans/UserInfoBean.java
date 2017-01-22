package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LiuChao
 * @describe 用户信息的实体类
 * @date 2017/1/11
 * @contact email:450127106@qq.com
 */

public class UserInfoBean implements Parcelable {
    private String name;
    private String userIcon;
    private String location;
    private String intro;
    private String sex;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.userIcon);
        dest.writeString(this.location);
        dest.writeString(this.intro);
        dest.writeString(this.sex);
        dest.writeString(this.userId);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.name = in.readString();
        this.userIcon = in.readString();
        this.location = in.readString();
        this.intro = in.readString();
        this.sex = in.readString();
        this.userId = in.readString();
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
