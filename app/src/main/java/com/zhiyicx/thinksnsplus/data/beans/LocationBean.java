package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public class LocationBean extends BaseListBean {


    /**
     * id : 2507
     * name : 四川省
     * pid : 1
     * extends :
     * created_at : 2017-06-02 08:44:10
     * updated_at : 2017-06-02 08:44:10
     * parent : null
     */

    private int id;
    private String name;
    private int pid;
    @SerializedName(value = "extendsX",alternate = {"extends"})
    private String extendsX;
    private String created_at;
    private String updated_at;
    private LocationBean parent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getExtendsX() {
        return extendsX;
    }

    public void setExtendsX(String extendsX) {
        this.extendsX = extendsX;
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

    public LocationBean getParent() {
        return parent;
    }

    public void setParent(LocationBean parent) {
        this.parent = parent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.pid);
        dest.writeString(this.extendsX);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.parent, flags);
    }

    public LocationBean() {
    }

    protected LocationBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        this.pid = in.readInt();
        this.extendsX = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.parent = in.readParcelable(LocationBean.class.getClassLoader());
    }

    public static final Creator<LocationBean> CREATOR = new Creator<LocationBean>() {
        @Override
        public LocationBean createFromParcel(Parcel source) {
            return new LocationBean(source);
        }

        @Override
        public LocationBean[] newArray(int size) {
            return new LocationBean[size];
        }
    };

    @Override
    public String toString() {
        return "LocationBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", extendsX='" + extendsX + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", parent=" + parent +
                '}';
    }

    /**
     * 获取当前地址
     *
     * @param locationBean
     * @return
     */
    public static String getlocation(LocationBean locationBean) {
        String location = getLocationString(locationBean);
        if (location.endsWith("，")) {
            location = location.substring(0, location.length() - 1);
        }
        return location;
    }

    @NonNull
    private static String getLocationString(LocationBean locationBean) {
        String location = "";
        if (locationBean.getParent() != null) {
            location = locationBean.getName() + "，" + location;
            location = getLocationString(locationBean.getParent()) + location;
        } else {
            location = locationBean.getName() + "，" + location;

        }
        return location;
    }

}