package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyCateBean;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

public class CandyWalletBean extends BaseListBean {
    private static final long serialVersionUID = 124L;

    @Id
    private Long id;
    @Unique
    private int user_id;
    private int candy_cat_id;
    private String count;
    private String created_at;
    private String updated_at;
    private CandyCateBean candy_cate;

    protected CandyWalletBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        user_id = in.readInt();
        candy_cat_id = in.readInt();
        count = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        candy_cate = in.readParcelable(CandyCateBean.class.getClassLoader());
    }

    public static final Creator<CandyWalletBean> CREATOR = new Creator<CandyWalletBean>() {
        @Override
        public CandyWalletBean createFromParcel(Parcel in) {
            return new CandyWalletBean(in);
        }

        @Override
        public CandyWalletBean[] newArray(int size) {
            return new CandyWalletBean[size];
        }
    };

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCandy_cat_id() {
        return candy_cat_id;
    }

    public void setCandy_cat_id(int candy_cat_id) {
        this.candy_cat_id = candy_cat_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public CandyCateBean getCandy_cate() {
        return candy_cate;
    }

    public void setCandy_cate(CandyCateBean candy_cate) {
        this.candy_cate = candy_cate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeInt(user_id);
        parcel.writeInt(candy_cat_id);
        parcel.writeString(count);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeParcelable(candy_cate, i);
    }
}
