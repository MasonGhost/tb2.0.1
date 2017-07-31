package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class UserTagBean extends BaseListBean {

    /**
     * {
     * "id": 1,
     * "name": "标签1",
     * "tag_category_id": 1
     * }
     */

    private Long id;
    @SerializedName("name")
    private String tagName;
    private long tag_category_id;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTag_category_id() {
        return tag_category_id;
    }

    public void setTag_category_id(long tag_category_id) {
        this.tag_category_id = tag_category_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.tagName);
        dest.writeLong(this.tag_category_id);
    }

    public UserTagBean() {
    }

    protected UserTagBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.tagName = in.readString();
        this.tag_category_id = in.readLong();
    }

    public static final Creator<UserTagBean> CREATOR = new Creator<UserTagBean>() {
        @Override
        public UserTagBean createFromParcel(Parcel source) {
            return new UserTagBean(source);
        }

        @Override
        public UserTagBean[] newArray(int size) {
            return new UserTagBean[size];
        }
    };

    @Override
    public String toString() {
        return "UserTagBean{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                ", tag_category_id=" + tag_category_id +
                '}';
    }
}
