package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author LiuChao
 * @describe 用户信息的实体类
 * @date 2017/1/11
 * @contact email:450127106@qq.com
 */


@Entity
public class UserInfoBean implements Parcelable {
    @Id
    private Long user_id;
    private int sex;
    private String name;
    private String userIcon;
    private String location;
    private int province;
    private int city;
    private int area;
    private String education;
    private String intro;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.user_id);
        dest.writeInt(this.sex);
        dest.writeString(this.name);
        dest.writeString(this.userIcon);
        dest.writeString(this.location);
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeInt(this.area);
        dest.writeString(this.education);
        dest.writeString(this.intro);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.sex = in.readInt();
        this.name = in.readString();
        this.userIcon = in.readString();
        this.location = in.readString();
        this.province = in.readInt();
        this.city = in.readInt();
        this.area = in.readInt();
        this.education = in.readString();
        this.intro = in.readString();
    }

    public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

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

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "user_id=" + user_id +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", location='" + location + '\'' +
                ", province=" + province +
                ", city=" + city +
                ", area=" + area +
                ", education='" + education + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
