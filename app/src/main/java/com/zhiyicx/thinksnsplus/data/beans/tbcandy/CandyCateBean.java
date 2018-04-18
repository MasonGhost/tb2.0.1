package com.zhiyicx.thinksnsplus.data.beans.tbcandy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lx on 2018/4/18.
 */

public class CandyCateBean implements Parcelable {
    private int id;
    private String cat_name;
    private int logo;
    private String mark;
    private int state;
    private int status;
    private String created_at;
    private String updated_at;

    protected CandyCateBean(Parcel in) {
        id = in.readInt();
        cat_name = in.readString();
        logo = in.readInt();
        mark = in.readString();
        state = in.readInt();
        status = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<CandyCateBean> CREATOR = new Creator<CandyCateBean>() {
        @Override
        public CandyCateBean createFromParcel(Parcel in) {
            return new CandyCateBean(in);
        }

        @Override
        public CandyCateBean[] newArray(int size) {
            return new CandyCateBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(cat_name);
        parcel.writeInt(logo);
        parcel.writeString(mark);
        parcel.writeInt(state);
        parcel.writeInt(status);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
    }
}
