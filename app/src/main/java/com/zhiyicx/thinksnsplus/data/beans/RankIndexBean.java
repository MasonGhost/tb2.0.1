package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankIndexBean extends BaseListBean implements Serializable{

    private static final long serialVersionUID = -6644496707464400961L;

    private String category; // 分类 如 用户
    private String subCategory; // 二级分类 如全站粉丝列表
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.category);
        dest.writeString(this.subCategory);
        dest.writeTypedList(this.userInfoList);
    }

    public RankIndexBean() {
    }

    protected RankIndexBean(Parcel in) {
        super(in);
        this.category = in.readString();
        this.subCategory = in.readString();
        this.userInfoList = in.createTypedArrayList(UserInfoBean.CREATOR);
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
