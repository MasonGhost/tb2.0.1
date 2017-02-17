package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author LiuChao
 * @describe 用户信息的实体类
 * @date 2017/1/11
 * @contact email:450127106@qq.com
 */


@Entity
public class UserInfoBean implements Parcelable {
    // 定义四种性别状态
    public static final String NOT_DEFINE = "0";
    public static final String MALE = "1";
    public static final String FEMALE = "2";
    public static final String SECRET = "3";
    @Id
    private Long user_id;
    private String sex = NOT_DEFINE;// 1 2 3  1男 2女 3其他
    @Transient
    private String sexString;// sex编号对应的具体值，不保存到数据库中
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
        dest.writeString(this.sex);
        dest.writeString(this.sexString);
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
        this.sex = in.readString();
        this.sexString = in.readString();
        this.name = in.readString();
        this.userIcon = in.readString();
        this.location = in.readString();
        this.province = in.readInt();
        this.city = in.readInt();
        this.area = in.readInt();
        this.education = in.readString();
        this.intro = in.readString();
    }

    @Generated(hash = 1499218000)
    public UserInfoBean(Long user_id, String sex, String name, String userIcon,
                        String location, int province, int city, int area, String education,
                        String intro) {
        this.user_id = user_id;
        this.sex = sex;
        this.name = name;
        this.userIcon = userIcon;
        this.location = location;
        this.province = province;
        this.city = city;
        this.area = area;
        this.education = education;
        this.intro = intro;
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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getSex() {
        if (sex == null) {
            return "";
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexString() {
        switch (sex) {
            case NOT_DEFINE:
                sexString = "";
                break;
            case MALE:
                sexString = AppApplication.getContext().getString(R.string.male);
                break;
            case FEMALE:
                sexString = AppApplication.getContext().getString(R.string.female);
                break;
            case SECRET:
                sexString = AppApplication.getContext().getString(R.string.keep_secret);
                break;
            default:
                sexString = "";
                break;
        }
        return sexString;
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserIcon() {
        if (userIcon == null) {
            return "";
        }
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getLocation() {
        if (location == null) {
            return "";
        }
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
        if (education == null) {
            return "";
        }
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIntro() {
        if (intro == null) {
            return "";
        }
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
