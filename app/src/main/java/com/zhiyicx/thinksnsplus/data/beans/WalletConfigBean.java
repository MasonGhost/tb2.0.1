package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */

public class WalletConfigBean implements Serializable, Parcelable {

    public static final String TYPE_ALIPAY = "alipay";
    public static final String TYPE_WECHAT = "wechat";
    private static final long serialVersionUID = 2408871831852484952L;
    /**
     * labels : [550,2000,9900]
     * ratio : 200
     * rule : 我是积分规则纯文本.
     * alipay : {"open":false}
     * apple : {"open":false}
     * wechat : {"open":false}
     * cash : {"types":["alipay"]}
     */

    private int ratio;
    private String rule;
    private String[] cash;
    private List<Float> labels;
    private String[] recharge_type;

    public String[] getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(String[] recharge_type) {
        this.recharge_type = recharge_type;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String[] getCash() {
        return cash;
    }

    public void setCash(String[] cash) {
        this.cash = cash;
    }

    public List<Float> getLabels() {
        return labels;
    }

    public void setLabels(List<Float> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "WalletConfigBean{" +
                "ratio=" + ratio +
                ", rule='" + rule + '\'' +
                ", cash=" + cash +
                ", labels=" + labels +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ratio);
        dest.writeString(this.rule);
        dest.writeStringArray(this.cash);
        dest.writeList(this.labels);
        dest.writeStringArray(this.recharge_type);
    }

    public WalletConfigBean() {
    }

    protected WalletConfigBean(Parcel in) {
        this.ratio = in.readInt();
        this.rule = in.readString();
        this.cash = in.createStringArray();
        this.labels = new ArrayList<Float>();
        in.readList(this.labels, Float.class.getClassLoader());
        this.recharge_type = in.createStringArray();
    }

    public static final Creator<WalletConfigBean> CREATOR = new Creator<WalletConfigBean>() {
        @Override
        public WalletConfigBean createFromParcel(Parcel source) {
            return new WalletConfigBean(source);
        }

        @Override
        public WalletConfigBean[] newArray(int size) {
            return new WalletConfigBean[size];
        }
    };
}
