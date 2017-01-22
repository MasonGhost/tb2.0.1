package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author LiuChao
 * @describe 用户信息的实体类
 * @date 2017/1/11
 * @contact email:450127106@qq.com
 */


@Entity
public class UserInfoBean implements Parcelable {
    private String name;
    private String userIcon;
    private String location;
    private String intro;
    private String sex;
    @Id
    private Long user_id;

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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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
        dest.writeValue(this.user_id);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.name = in.readString();
        this.userIcon = in.readString();
        this.location = in.readString();
        this.intro = in.readString();
        this.sex = in.readString();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 194228155)
    public UserInfoBean(String name, String userIcon, String location, String intro,
            String sex, Long user_id) {
        this.name = name;
        this.userIcon = userIcon;
        this.location = location;
        this.intro = intro;
        this.sex = sex;
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "name='" + name + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", location='" + location + '\'' +
                ", intro='" + intro + '\'' +
                ", sex='" + sex + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
