package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;


/**
 * @Describe 文档查阅 @see{ https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/user/show.md }
 * @Author Jungle68
 * @Date 2017/7/18
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class UserInfoBean extends CacheBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 536871008;

    /**
     * {
     * "id": 1, // 用户id
     * "name": "创始人", // 用户名
     * "phone": "187xxxxxxxx", // 用户手机号码
     * "email": "shiweidu@outlook.com" // 用户邮箱
     * "bio": "我是大管理员", // 用户简介
     * "sex": 0, // 用户性别，0 - 未知，1 - 男，2 - 女
     * "location": "成都市 四川省 中国", // 用户位置
     * "created_at": "2017-06-02 08:43:54",
     * "updated_at": "2017-07-06 07:04:06",
     * "avatar": "http://plus.io/api/v2/users/1/avatar", // 头像
     * "extra": {
     * "user_id": 1,
     * "likes_count": 0, // 被喜欢统计数
     * "comments_count": 0, // 用户发出的评论统计
     * "followers_count": 0, // 用户粉丝数
     * "followings_count": 1, // 用户关注数
     * "updated_at": "2017-07-16 09:44:25", // 更新时间
     * "feeds_count": 0 // 发布的动态统计，没有安装 动态应用则不存在
     * },
     * "wallet": {
     * "id": 1,
     * "user_id": 1,
     * "balance": 90, // 用户余额
     * "created_at": "2017-06-02 08:43:54",
     * "updated_at": "2017-07-05 08:29:49",
     * "deleted_at": null
     * }
     * }
     */
    // 定义四种性别状态
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int SECRET = 0;
    @Id
    @SerializedName("id")
    private Long user_id;
    private String name;
    private String phone;
    private String email;
    @SerializedName("bio")
    private String intro;
    private int sex;            // 1 2 3  1男 2女 3其他
    @Transient
    private String sexString;   // sex编号对应的具体值，不保存到数据库中
    private String location;
    @Transient
    private String province;    // 省
    @Transient
    private String city;        // 城市
    @Transient
    private String area;        // 区


    private String created_at;
    private String updated_at;
    private String avatar;      // 头像 地址
    private String cover;// 封面
    @Transient
    private WalletBean wallet;

    @Convert(converter = ExtraParamsConverter.class, columnType = String.class)
    private UserInfoExtraBean extra;


    public String getSexString() {
        switch (getSex()) {
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

    public void setSexString(String sexString) {
        this.sexString = sexString;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvince() {
        if (TextUtils.isEmpty(province)&&!TextUtils.isEmpty(location)) {
            String[] data = location.split(" ");
            if (data.length > 0) {
                province = data[0];
            }
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        if (TextUtils.isEmpty(city)&&!TextUtils.isEmpty(location)) {
            String[] data = location.split(" ");
            if (data.length > 1) {
                city = data[1];
            }
        }

        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        if (TextUtils.isEmpty(area)&&!TextUtils.isEmpty(location)) {
            String[] data = location.split(" ");
            if (data.length > 2) {
                area = data[2];
            }
        }
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public WalletBean getWallet() {
        return wallet;
    }

    public void setWallet(WalletBean wallet) {
        this.wallet = wallet;
    }

    public UserInfoExtraBean getExtra() {
        return extra;
    }

    public void setExtra(UserInfoExtraBean extra) {
        this.extra = extra;
    }

    public UserInfoBean() {
    }

    /**
     * {
     * "user_id": 1,
     * "likes_count": 0, // 被喜欢统计数
     * "comments_count": 0, // 用户发出的评论统计
     * "followers_count": 0, // 用户粉丝数
     * "followings_count": 1, // 用户关注数
     * "updated_at": "2017-07-16 09:44:25", // 更新时间
     * "feeds_count": 0 // 发布的动态统计，没有安装 动态应用则不存在
     * }
     */
    public static class UserInfoExtraBean implements Serializable, Parcelable {
        private static final long serialVersionUID = 8468324804308698269L;
        private Long user_id;
        private int likes_count;
        private int comments_count;
        private int followers_count;
        private int followings_count;
        private int feeds_count;
        private String updated_at;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.user_id);
            dest.writeInt(this.likes_count);
            dest.writeInt(this.comments_count);
            dest.writeInt(this.followers_count);
            dest.writeInt(this.followings_count);
            dest.writeInt(this.feeds_count);
            dest.writeString(this.updated_at);
        }

        public UserInfoExtraBean() {
        }

        protected UserInfoExtraBean(Parcel in) {
            this.user_id = (Long) in.readValue(Long.class.getClassLoader());
            this.likes_count = in.readInt();
            this.comments_count = in.readInt();
            this.followers_count = in.readInt();
            this.followings_count = in.readInt();
            this.feeds_count = in.readInt();
            this.updated_at = in.readString();
        }

        public static final Creator<UserInfoExtraBean> CREATOR = new Creator<UserInfoExtraBean>() {
            @Override
            public UserInfoExtraBean createFromParcel(Parcel source) {
                return new UserInfoExtraBean(source);
            }

            @Override
            public UserInfoExtraBean[] newArray(int size) {
                return new UserInfoExtraBean[size];
            }
        };

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public int getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(int followers_count) {
            this.followers_count = followers_count;
        }

        public int getFollowings_count() {
            return followings_count;
        }

        public void setFollowings_count(int followings_count) {
            this.followings_count = followings_count;
        }

        public int getFeeds_count() {
            return feeds_count;
        }

        public void setFeeds_count(int feeds_count) {
            this.feeds_count = feeds_count;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        @Override
        public String toString() {
            return "UserInfoExtraBean{" +
                    "user_id=" + user_id +
                    ", likes_count=" + likes_count +
                    ", comments_count=" + comments_count +
                    ", followers_count=" + followers_count +
                    ", followings_count=" + followings_count +
                    ", feeds_count=" + feeds_count +
                    ", updated_at='" + updated_at + '\'' +
                    '}';
        }
    }


    /**
     * UserInfoExtraBean 转 String 形式存入数据库
     */
    public static class ExtraParamsConverter implements PropertyConverter<UserInfoExtraBean, String> {

        @Override
        public UserInfoExtraBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(UserInfoExtraBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.intro);
        dest.writeInt(this.sex);
        dest.writeString(this.sexString);
        dest.writeString(this.location);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.avatar);
        dest.writeString(this.cover);
        dest.writeParcelable(this.wallet, flags);
        dest.writeParcelable(this.extra, flags);
    }

    protected UserInfoBean(Parcel in) {
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.intro = in.readString();
        this.sex = in.readInt();
        this.sexString = in.readString();
        this.location = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.avatar = in.readString();
        this.cover = in.readString();
        this.wallet = in.readParcelable(WalletBean.class.getClassLoader());
        this.extra = in.readParcelable(UserInfoExtraBean.class.getClassLoader());
    }

    @Generated(hash = 923884662)
    public UserInfoBean(Long user_id, String name, String phone, String email, String intro, int sex,
            String location, String created_at, String updated_at, String avatar, String cover,
            UserInfoExtraBean extra) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.intro = intro;
        this.sex = sex;
        this.location = location;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.avatar = avatar;
        this.cover = cover;
        this.extra = extra;
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
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", intro='" + intro + '\'' +
                ", sex=" + sex +
                ", sexString='" + sexString + '\'' +
                ", location='" + location + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", wallet=" + wallet +
                ", extra=" + extra +
                '}';
    }
}
