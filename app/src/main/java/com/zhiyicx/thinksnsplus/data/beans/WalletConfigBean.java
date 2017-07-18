package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.thinksnsplus.data.source.local.data_convert.FloatListConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.StringArrayConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class WalletConfigBean implements Serializable, Parcelable {

    public static final String TYPE_ALIPAY = "alipay";
    public static final String TYPE_WECHAT = "wechat";
    private static final long serialVersionUID = 2408871831852484952L;
    /**
     * labels : [550,2000,9900]
     * ratio : 200
     * rule : 我是积分规则纯文本.
     * case_min_amount : 1, // 真实金额分单位，用户最低提现金额。
     * alipay : {"open":false}
     * apple : {"open":false}
     * wechat : {"open":false}
     * cash : {"types":["alipay"]}
     */
    @Id
    @Unique
    private Long user_id;
    private int ratio;
    private int case_min_amount;
    private String rule;
    @Convert(converter = StringArrayConvert.class, columnType = String.class)
    private String[] cash;
    @Convert(converter = FloatListConvert.class, columnType = String.class)
    private List<Float> labels;
    @Convert(converter = StringArrayConvert.class, columnType = String.class)
    private String[] recharge_type;

    public String[] getRecharge_type() {
        return recharge_type;
    }

    public int getCase_min_amount() {
        return case_min_amount;
    }

    public void setCase_min_amount(int case_min_amount) {
        this.case_min_amount = case_min_amount;
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
        dest.writeValue(this.user_id);
        dest.writeInt(this.ratio);
        dest.writeInt(this.case_min_amount);
        dest.writeString(this.rule);
        dest.writeStringArray(this.cash);
        dest.writeList(this.labels);
        dest.writeStringArray(this.recharge_type);
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public WalletConfigBean() {
    }

    protected WalletConfigBean(Parcel in) {
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.ratio = in.readInt();
        this.case_min_amount = in.readInt();
        this.rule = in.readString();
        this.cash = in.createStringArray();
        this.labels = new ArrayList<Float>();
        in.readList(this.labels, Float.class.getClassLoader());
        this.recharge_type = in.createStringArray();
    }

    @Generated(hash = 342150229)
    public WalletConfigBean(Long user_id, int ratio, int case_min_amount, String rule,
            String[] cash, List<Float> labels, String[] recharge_type) {
        this.user_id = user_id;
        this.ratio = ratio;
        this.case_min_amount = case_min_amount;
        this.rule = rule;
        this.cash = cash;
        this.labels = labels;
        this.recharge_type = recharge_type;
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
