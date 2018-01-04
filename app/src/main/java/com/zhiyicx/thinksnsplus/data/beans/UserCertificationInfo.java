package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

import retrofit2.http.PUT;

/**
 * @author Catherine
 * @describe 用户认证信息
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */
@Entity
public class UserCertificationInfo extends BaseListBean {

    /*{
        "id": 1,
            "certification_name": "user",
            "user_id": 1,
            "data": {
                "name": "杜伟",
                "phone": "18781993582",
                "number": "xxxxxxxxxx",
                "desc": "hahaha",
                "file": 12,
                "org_name": "之一程序",
                "org_address": null
            },
        "examiner": 0,
            "status": 0,
            "created_at": "2017-07-22 06:22:49",
            "updated_at": "2017-07-22 06:45:57",
            "icon": null,
            "category": {
                "name": "user",
                "display_name": "个人认证",
                "description": null
            }
    }*/

    /**
     * 审核状态
     * 2-被驳回 0-审核 1-通过
     */
    public enum CertifyStatusEnum {
        REJECTED(2),
        REVIEWING(0),
        PASS(1);
        public int value;

        CertifyStatusEnum(int value) {
            this.value = value;
        }
    }

    @Id
    private long id;
    private long user_id;
    private String certification_name;
    @Convert(converter = DataConvert.class, columnType = String.class)
    private UserCertificationData data;
    private long examiner;
    private long status; // 2-被驳回 0-审核 1-通过
    private String created_at;
    private String updated_at;
    @Convert(converter = CategoryConvert.class, columnType = String.class)
    private CertificationCategory category;
    private String icon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getCertification_name() {
        return certification_name;
    }

    public void setCertification_name(String certification_name) {
        this.certification_name = certification_name;
    }

    public UserCertificationData getData() {
        return data;
    }

    public void setData(UserCertificationData data) {
        this.data = data;
    }

    public long getExaminer() {
        return examiner;
    }

    public void setExaminer(long examiner) {
        this.examiner = examiner;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
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

    public CertificationCategory getCategory() {
        return category;
    }

    public void setCategory(CertificationCategory category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "UserCertificationInfo{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", certification_name='" + certification_name + '\'' +
                ", data=" + data +
                ", examiner=" + examiner +
                ", status=" + status +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", category=" + category +
                ", icon='" + icon + '\'' +
                '}';
    }

    public static class UserCertificationData implements Parcelable, Serializable {

        private static final long serialVersionUID = 522802720451708781L;

        private String name;
        private String phone;
        private String number;
        private String desc; // 备注
        private String org_name;
        private String org_address;
        private List<Integer> files;

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

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getOrg_name() {
            return org_name;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }

        public String getOrg_address() {
            return org_address;
        }

        public void setOrg_address(String org_address) {
            this.org_address = org_address;
        }

        public List<Integer> getFiles() {
            return files;
        }

        public void setFiles(List<Integer> files) {
            this.files = files;
        }

        @Override
        public String toString() {
            return "UserCertificationData{" +
                    "name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", number='" + number + '\'' +
                    ", desc='" + desc + '\'' +
                    ", org_name='" + org_name + '\'' +
                    ", org_address='" + org_address + '\'' +
                    ", files=" + files +
                    '}';
        }

        public UserCertificationData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.phone);
            dest.writeString(this.number);
            dest.writeString(this.desc);
            dest.writeString(this.org_name);
            dest.writeString(this.org_address);
            dest.writeList(this.files);
        }

        protected UserCertificationData(Parcel in) {
            this.name = in.readString();
            this.phone = in.readString();
            this.number = in.readString();
            this.desc = in.readString();
            this.org_name = in.readString();
            this.org_address = in.readString();
            this.files = new ArrayList<Integer>();
            in.readList(this.files, Integer.class.getClassLoader());
        }

        public static final Creator<UserCertificationData> CREATOR = new Creator<UserCertificationData>() {
            @Override
            public UserCertificationData createFromParcel(Parcel source) {
                return new UserCertificationData(source);
            }

            @Override
            public UserCertificationData[] newArray(int size) {
                return new UserCertificationData[size];
            }
        };
    }

    public static class DataConvert extends BaseConvert<UserCertificationData> {

    }

    public static class CertificationCategory implements Parcelable, Serializable {

        private static final long serialVersionUID = 3669070613696725049L;

        private String name;
        private String display_name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "CertificationCategory{" +
                    "name='" + name + '\'' +
                    ", display_name='" + display_name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.display_name);
            dest.writeString(this.description);
        }

        public CertificationCategory() {
        }

        protected CertificationCategory(Parcel in) {
            this.name = in.readString();
            this.display_name = in.readString();
            this.description = in.readString();
        }

        public static final Creator<CertificationCategory> CREATOR = new Creator<CertificationCategory>() {
            @Override
            public CertificationCategory createFromParcel(Parcel source) {
                return new CertificationCategory(source);
            }

            @Override
            public CertificationCategory[] newArray(int size) {
                return new CertificationCategory[size];
            }
        };
    }

    public static class CategoryConvert extends BaseConvert<CertificationCategory> {

    }

    public UserCertificationInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.id);
        dest.writeLong(this.user_id);
        dest.writeString(this.certification_name);
        dest.writeParcelable(this.data, flags);
        dest.writeLong(this.examiner);
        dest.writeLong(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.category, flags);
        dest.writeString(this.icon);
    }

    protected UserCertificationInfo(Parcel in) {
        super(in);
        this.id = in.readLong();
        this.user_id = in.readLong();
        this.certification_name = in.readString();
        this.data = in.readParcelable(UserCertificationData.class.getClassLoader());
        this.examiner = in.readLong();
        this.status = in.readLong();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.category = in.readParcelable(CertificationCategory.class.getClassLoader());
        this.icon = in.readString();
    }

    @Generated(hash = 22762421)
    public UserCertificationInfo(long id, long user_id, String certification_name, UserCertificationData data,
                                 long examiner, long status, String created_at, String updated_at, CertificationCategory category,
                                 String icon) {
        this.id = id;
        this.user_id = user_id;
        this.certification_name = certification_name;
        this.data = data;
        this.examiner = examiner;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.category = category;
        this.icon = icon;
    }

    public static final Creator<UserCertificationInfo> CREATOR = new Creator<UserCertificationInfo>() {
        @Override
        public UserCertificationInfo createFromParcel(Parcel source) {
            return new UserCertificationInfo(source);
        }

        @Override
        public UserCertificationInfo[] newArray(int size) {
            return new UserCertificationInfo[size];
        }
    };
}
