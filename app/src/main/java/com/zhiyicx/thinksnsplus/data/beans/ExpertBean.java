package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Catherine
 * @describe 专家bean
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertBean extends BaseListBean{

    private long id;
    private String name;
    private long avatar;
    private int anwser_count;
    private int dig_count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAvatar() {
        return avatar;
    }

    public void setAvatar(long avatar) {
        this.avatar = avatar;
    }

    public int getAnwser_count() {
        return anwser_count;
    }

    public void setAnwser_count(int anwser_count) {
        this.anwser_count = anwser_count;
    }

    public int getDig_count() {
        return dig_count;
    }

    public void setDig_count(int dig_count) {
        this.dig_count = dig_count;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.avatar);
        dest.writeInt(this.anwser_count);
        dest.writeInt(this.dig_count);
    }

    public ExpertBean() {
    }

    protected ExpertBean(Parcel in) {
        super(in);
        this.id = in.readLong();
        this.name = in.readString();
        this.avatar = in.readLong();
        this.anwser_count = in.readInt();
        this.dig_count = in.readInt();
    }

    public static final Creator<ExpertBean> CREATOR = new Creator<ExpertBean>() {
        @Override
        public ExpertBean createFromParcel(Parcel source) {
            return new ExpertBean(source);
        }

        @Override
        public ExpertBean[] newArray(int size) {
            return new ExpertBean[size];
        }
    };
}
