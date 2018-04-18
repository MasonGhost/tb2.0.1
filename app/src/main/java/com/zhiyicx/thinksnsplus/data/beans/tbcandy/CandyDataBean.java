package com.zhiyicx.thinksnsplus.data.beans.tbcandy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/18.
 */
public class CandyDataBean implements Parcelable {
    private String prject_name;
    private String prject_desc;
    private String link;

    protected CandyDataBean(Parcel in) {
        prject_name = in.readString();
        prject_desc = in.readString();
        link = in.readString();
    }

    public static final Creator<CandyDataBean> CREATOR = new Creator<CandyDataBean>() {
        @Override
        public CandyDataBean createFromParcel(Parcel in) {
            return new CandyDataBean(in);
        }

        @Override
        public CandyDataBean[] newArray(int size) {
            return new CandyDataBean[size];
        }
    };

    public String getPrject_name() {
        return prject_name;
    }

    public void setPrject_name(String prject_name) {
        this.prject_name = prject_name;
    }

    public String getPrject_desc() {
        return prject_desc;
    }

    public void setPrject_desc(String prject_desc) {
        this.prject_desc = prject_desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(prject_name);
        parcel.writeString(prject_desc);
        parcel.writeString(link);
    }

    @Override
    public String toString() {
        return "CandyDataBean{" +
                "prject_name=" + prject_name + '\'' +
                ", prject_desc=" + prject_desc + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
