package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/30
 * @Contact master.jungle68@gmail.com
 */

public class ComponentConfigBean implements Parcelable,Serializable {
    private static final long serialVersionUID = 53687200;
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.value);
    }

    public ComponentConfigBean() {
    }

    protected ComponentConfigBean(Parcel in) {
        this.name = in.readString();
        this.value = in.readString();
    }

    public static final Creator<ComponentConfigBean> CREATOR = new Creator<ComponentConfigBean>() {
        @Override
        public ComponentConfigBean createFromParcel(Parcel source) {
            return new ComponentConfigBean(source);
        }

        @Override
        public ComponentConfigBean[] newArray(int size) {
            return new ComponentConfigBean[size];
        }
    };

    @Override
    public String toString() {
        return "ComponentConfigBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
