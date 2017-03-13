package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 用户信息的实体类  具体查看文档 {@see https://github.com/zhiyicx/thinksns-plus/blob/master/documents/api/v1/%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E8%8E%B7%E5%8F%96.md}
 * @date 2017/1/11
 * @contact email:450127106@qq.com
 */


@Entity
public class UserInfoBean implements Parcelable ,Serializable{
    private static final long serialVersionUID = 536871008;
    // 定义四种性别状态
    public static final String MALE = "1";
    public static final String FEMALE = "2";
    public static final String SECRET = "3";
    @Id
    @SerializedName("id")
    private Long user_id;
    private String sex;// 1 2 3  1男 2女 3其他
    @Transient
    private String sexString;// sex编号对应的具体值，不保存到数据库中
    private String name;
    private String avatar;  // 投降 id
    private String phone;
    private String email;
    private String intro;
    private String location;
    private String province;
    private String city;
    private String area;
    private String education;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String diggs_count;// 点赞数量
    private String following_count;// 关注数量
    private String followed_count;// 粉丝数量
    private String feeds_count;// 动态数量
    private String cover;// 封面

    /**
     * id : 9
     * profile : avatar
     * profile_name : 用户头像
     * type : input
     * default_options :
     * pivot : {"user_id":5,"user_profile_setting_id":9,"user_profile_setting_data":"50","created_at":1231313123131231,"updated_at":1231313123131231}
     */
    @Convert(converter = DataConverter.class, columnType = String.class)
    private List<DatasBean> datas;
    /**
     * id : 2
     * user_id : 5
     * key : diggs_count
     * value : 6
     * created_at : 1231313123131231
     * updated_at : 1231313123131231
     */
    @Convert(converter = CountConverter.class, columnType = String.class)
    private List<CountsBean> counts;


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
        return avoidNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        if (cover == null) {
            cover = getConfigProperty("cover");
        }
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDiggs_count() {
        if (diggs_count == null) {
            diggs_count = getCountProperty("diggs_count");
        }
        return diggs_count;
    }

    public void setDiggs_count(String diggs_count) {
        this.diggs_count = diggs_count;
    }

    public String getFollowing_count() {
        if (following_count == null) {
            following_count = getCountProperty("following_count");
        }
        return following_count;
    }

    public void setFollowing_count(String following_count) {
        this.following_count = following_count;
    }

    public String getFollowed_count() {
        if (followed_count == null) {
            followed_count = getCountProperty("followed_count");
        }
        return followed_count;
    }

    public void setFollowed_count(String followed_count) {
        this.followed_count = followed_count;
    }

    public String getFeeds_count() {
        if (feeds_count == null) {
            feeds_count = getCountProperty("feeds_count");
        }
        return feeds_count;
    }

    public void setFeeds_count(String feeds_count) {
        this.feeds_count = feeds_count;
    }

    public String getSex() {
        if (sex == null) {
            sex = getConfigProperty("sex");
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        if (avatar == null) {
            avatar = getConfigProperty("avatar");
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        if (location == null) {
            location = getConfigProperty("location");
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProvince() {
        if (province == null) {
            province = getConfigProperty("province");
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        if (city == null) {
            city = getConfigProperty("city");
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        if (area == null) {
            area = getConfigProperty("area");
        }
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEducation() {
        if (education == null) {
            education = getConfigProperty("education");
        }
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIntro() {
        if (intro == null) {
            intro = getConfigProperty("intro");
        }
        // 如果依然没有简介，那就返回缺省的内容
        if (TextUtils.isEmpty(intro)) {
            intro = AppApplication.getContext().getString(R.string.intro_default);
        }
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getEmail() {
        return avoidNull(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return avoidNull(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public List<DatasBean> getDatas() {
        datas = datas == null ? new ArrayList<DatasBean>() : datas;
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public List<CountsBean> getCounts() {
        counts = counts == null ? new ArrayList<CountsBean>() : counts;
        return counts;
    }

    public void setCounts(List<CountsBean> counts) {
        this.counts = counts;
    }

    public static class DatasBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871008L;
        private int id;
        private String profile;
        private String profile_name;
        private String type;
        private String default_options;
        /**
         * user_id : 5
         * user_profile_setting_id : 9
         * user_profile_setting_data : 50
         * created_at : 1231313123131231
         * updated_at : 1231313123131231
         */

        private PivotBean pivot;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getProfile_name() {
            return profile_name;
        }

        public void setProfile_name(String profile_name) {
            this.profile_name = profile_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDefault_options() {
            return default_options;
        }

        public void setDefault_options(String default_options) {
            this.default_options = default_options;
        }

        public PivotBean getPivot() {
            return pivot;
        }

        public void setPivot(PivotBean pivot) {
            this.pivot = pivot;
        }

        public static class PivotBean implements Parcelable, Serializable {
            private static final long serialVersionUID = 536871009L;
            private int user_id;
            private int user_profile_setting_id;
            private String user_profile_setting_data;
            private String created_at;
            private String updated_at;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getUser_profile_setting_id() {
                return user_profile_setting_id;
            }

            public void setUser_profile_setting_id(int user_profile_setting_id) {
                this.user_profile_setting_id = user_profile_setting_id;
            }

            public String getUser_profile_setting_data() {
                return user_profile_setting_data;
            }

            public void setUser_profile_setting_data(String user_profile_setting_data) {
                this.user_profile_setting_data = user_profile_setting_data;
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

            public PivotBean() {
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.user_id);
                dest.writeInt(this.user_profile_setting_id);
                dest.writeString(this.user_profile_setting_data);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
            }

            protected PivotBean(Parcel in) {
                this.user_id = in.readInt();
                this.user_profile_setting_id = in.readInt();
                this.user_profile_setting_data = in.readString();
                this.created_at = in.readString();
                this.updated_at = in.readString();
            }

            public static final Creator<PivotBean> CREATOR = new Creator<PivotBean>() {
                @Override
                public PivotBean createFromParcel(Parcel source) {
                    return new PivotBean(source);
                }

                @Override
                public PivotBean[] newArray(int size) {
                    return new PivotBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.profile);
            dest.writeString(this.profile_name);
            dest.writeString(this.type);
            dest.writeString(this.default_options);
            dest.writeParcelable(this.pivot, flags);
        }

        public DatasBean() {
        }

        protected DatasBean(Parcel in) {
            this.id = in.readInt();
            this.profile = in.readString();
            this.profile_name = in.readString();
            this.type = in.readString();
            this.default_options = in.readString();
            this.pivot = in.readParcelable(PivotBean.class.getClassLoader());
        }

        public static final Creator<DatasBean> CREATOR = new Creator<DatasBean>() {
            @Override
            public DatasBean createFromParcel(Parcel source) {
                return new DatasBean(source);
            }

            @Override
            public DatasBean[] newArray(int size) {
                return new DatasBean[size];
            }
        };
    }

    public static class CountsBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871010L;
        private int id;
        private int user_id;
        private String key;
        private String value;
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

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
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
            dest.writeString(this.key);
            dest.writeString(this.value);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
        }

        public CountsBean() {
        }

        protected CountsBean(Parcel in) {
            this.id = in.readInt();
            this.user_id = in.readInt();
            this.key = in.readString();
            this.value = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
        }

        public static final Creator<CountsBean> CREATOR = new Creator<CountsBean>() {
            @Override
            public CountsBean createFromParcel(Parcel source) {
                return new CountsBean(source);
            }

            @Override
            public CountsBean[] newArray(int size) {
                return new CountsBean[size];
            }
        };
    }


    @Override
    public String toString() {
        return "UserInfoBean{" +
                "user_id=" + user_id +
                ", sex='" + sex + '\'' +
                ", sexString='" + sexString + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", intro='" + intro + '\'' +
                ", location='" + location + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", education='" + education + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", deleted_at=" + deleted_at +
                ", datas=" + datas +
                ", counts=" + counts +
                '}';
    }

    private String avoidNull(String data) {
        return data == null ? "" : data;
    }

    /**
     * list<DatasBean> 转 String 形式存入数据库
     */
    public static class DataConverter implements PropertyConverter<List<DatasBean>, String> {

        @Override
        public List<DatasBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<DatasBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    /**
     * list<CountsBean> 转 String 形式存入数据库
     */
    public static class CountConverter implements PropertyConverter<List<CountsBean>, String> {

        @Override
        public List<CountsBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<CountsBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    /**
     * 获取配置属性
     *
     * @param name
     * @return
     */
    private String getConfigProperty(String name) {
        String result = null;
        try {
            datas = getDatas(); // avoid null
            for (DatasBean data : datas) {
                if (data.getProfile().equals(name)) {
                    result = data.getPivot().getUser_profile_setting_data();
                    break;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result == null ? "" : result;
    }

    /**
     * 获取用户相关的数量统计字段
     *
     * @param name
     * @return
     */
    private String getCountProperty(String name) {
        String result = null;
        try {
            counts = getCounts(); // avoid null
            for (CountsBean count : counts) {
                if (count.getKey().equals(name)) {
                    result = count.getValue();
                    break;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result == null ? "" : result;
    }

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
        dest.writeString(this.avatar);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.intro);
        dest.writeString(this.location);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.education);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeTypedList(this.datas);
        dest.writeTypedList(this.counts);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.sex = in.readString();
        this.sexString = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.intro = in.readString();
        this.location = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.education = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.datas = in.createTypedArrayList(DatasBean.CREATOR);
        this.counts = in.createTypedArrayList(CountsBean.CREATOR);
    }

    @Generated(hash = 1124713812)
    public UserInfoBean(Long user_id, String sex, String name, String avatar, String phone, String email, String intro, String location, String province, String city,
                        String area, String education, String created_at, String updated_at, String deleted_at, String diggs_count, String following_count, String followed_count,
                        String feeds_count, String cover, List<DatasBean> datas, List<CountsBean> counts) {
        this.user_id = user_id;
        this.sex = sex;
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.intro = intro;
        this.location = location;
        this.province = province;
        this.city = city;
        this.area = area;
        this.education = education;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.diggs_count = diggs_count;
        this.following_count = following_count;
        this.followed_count = followed_count;
        this.feeds_count = feeds_count;
        this.cover = cover;
        this.datas = datas;
        this.counts = counts;
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
