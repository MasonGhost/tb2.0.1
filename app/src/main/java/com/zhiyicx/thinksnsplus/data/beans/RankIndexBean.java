package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */
@Entity
public class RankIndexBean extends BaseListBean implements Serializable{

    @Transient
    private static final long serialVersionUID = -6644496707464400961L;

    @Id(autoincrement = true)
    private Long id;
    private String category; // 分类 如 用户
    private String subCategory; // 二级分类 如全站粉丝列表
    @Unique
    private String type; // 分类
    @Convert(converter = UserListConver.class, columnType = String.class)
    private List<UserInfoBean> userInfoList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<UserInfoBean> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfoBean> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RankIndexBean{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", type='" + type + '\'' +
                ", userInfoList=" + userInfoList +
                '}';
    }

    public RankIndexBean() {
    }

    public static class UserListConver extends BaseConvert<List<UserInfoBean>>{}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.category);
        dest.writeString(this.subCategory);
        dest.writeString(this.type);
        dest.writeTypedList(this.userInfoList);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected RankIndexBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.category = in.readString();
        this.subCategory = in.readString();
        this.type = in.readString();
        this.userInfoList = in.createTypedArrayList(UserInfoBean.CREATOR);
    }

    @Generated(hash = 1058958724)
    public RankIndexBean(Long id, String category, String subCategory, String type,
            List<UserInfoBean> userInfoList) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
        this.type = type;
        this.userInfoList = userInfoList;
    }

    public static final Creator<RankIndexBean> CREATOR = new Creator<RankIndexBean>() {
        @Override
        public RankIndexBean createFromParcel(Parcel source) {
            return new RankIndexBean(source);
        }

        @Override
        public RankIndexBean[] newArray(int size) {
            return new RankIndexBean[size];
        }
    };
}
