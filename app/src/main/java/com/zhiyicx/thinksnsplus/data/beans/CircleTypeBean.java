package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * @author Jliuer
 * @Date 2017/11/27/16:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class CircleTypeBean extends BaseListBean implements Serializable{

    private static final long serialVersionUID = -7537055682609009723L;
    @Id
    private Long id;
    private long sort_by;
    private String name;
    private String created_at;
    private String updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getSort_by() {
        return sort_by;
    }

    public void setSort_by(long sort_by) {
        this.sort_by = sort_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.sort_by);
        dest.writeString(this.name);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public CircleTypeBean() {
    }

    protected CircleTypeBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.sort_by = in.readLong();
        this.name = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    @Generated(hash = 670162041)
    public CircleTypeBean(Long id, long sort_by, String name, String created_at,
                          String updated_at) {
        this.id = id;
        this.sort_by = sort_by;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static final Creator<CircleTypeBean> CREATOR = new Creator<CircleTypeBean>() {
        @Override
        public CircleTypeBean createFromParcel(Parcel source) {
            return new CircleTypeBean(source);
        }

        @Override
        public CircleTypeBean[] newArray(int size) {
            return new CircleTypeBean[size];
        }
    };
}
