package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/1
 * @Contact master.jungle68@gmail.com
 */
public class RewardsCountBean extends CacheBean implements Parcelable,Serializable{


    private static final long serialVersionUID = -8369008845865882648L;
    /**
     * count : 3
     * amount : 630
     */

    private int count;
    private String amount; // 分为单位
    private String moneyName;

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.amount);
        dest.writeString(this.moneyName);
    }

    public RewardsCountBean() {
    }

    public RewardsCountBean(int count, String amount,String moneyName) {
        this.count = count;
        this.amount = amount;
        this.moneyName = moneyName;
    }

    public RewardsCountBean(int count, String amount) {
        this.count = count;
        this.amount = amount;
    }

    protected RewardsCountBean(Parcel in) {
        this.count = in.readInt();
        this.amount = in.readString();
        this.moneyName = in.readString();
    }

    public static final Parcelable.Creator<RewardsCountBean> CREATOR = new Parcelable.Creator<RewardsCountBean>() {
        @Override
        public RewardsCountBean createFromParcel(Parcel source) {
            return new RewardsCountBean(source);
        }

        @Override
        public RewardsCountBean[] newArray(int size) {
            return new RewardsCountBean[size];
        }
    };
}
