package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */

public class WalletConfigBean  implements Serializable, Parcelable {


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
    private PayBean alipay;
    private PayBean apple;
    private PayBean wechat;
    private CashBean cash;
    private List<Integer> labels;

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

    public PayBean getAlipay() {
        return alipay;
    }

    public void setAlipay(PayBean alipay) {
        this.alipay = alipay;
    }

    public PayBean getApple() {
        return apple;
    }

    public void setApple(PayBean apple) {
        this.apple = apple;
    }

    public PayBean getWechat() {
        return wechat;
    }

    public void setWechat(PayBean wechat) {
        this.wechat = wechat;
    }

    public CashBean getCash() {
        return cash;
    }

    public void setCash(CashBean cash) {
        this.cash = cash;
    }

    public List<Integer> getLabels() {
        return labels;
    }

    public void setLabels(List<Integer> labels) {
        this.labels = labels;
    }

    public static class PayBean implements Serializable, Parcelable {
        private static final long serialVersionUID = -252453619898472184L;
        /**
         * open : false
         */

        private boolean open;

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.open ? (byte) 1 : (byte) 0);
        }

        public PayBean() {
        }

        protected PayBean(Parcel in) {
            this.open = in.readByte() != 0;
        }

        public static final Parcelable.Creator<PayBean> CREATOR = new Parcelable.Creator<PayBean>() {
            @Override
            public PayBean createFromParcel(Parcel source) {
                return new PayBean(source);
            }

            @Override
            public PayBean[] newArray(int size) {
                return new PayBean[size];
            }
        };
    }

    public static class CashBean implements Serializable, Parcelable {
        private static final long serialVersionUID = -113289545366067874L;
        private List<String> types;

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(this.types);
        }

        public CashBean() {
        }

        protected CashBean(Parcel in) {
            this.types = in.createStringArrayList();
        }

        public static final Parcelable.Creator<CashBean> CREATOR = new Parcelable.Creator<CashBean>() {
            @Override
            public CashBean createFromParcel(Parcel source) {
                return new CashBean(source);
            }

            @Override
            public CashBean[] newArray(int size) {
                return new CashBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ratio);
        dest.writeString(this.rule);
        dest.writeParcelable(this.alipay, flags);
        dest.writeParcelable(this.apple, flags);
        dest.writeParcelable(this.wechat, flags);
        dest.writeParcelable(this.cash, flags);
        dest.writeList(this.labels);
    }

    public WalletConfigBean() {
    }

    protected WalletConfigBean(Parcel in) {
        this.ratio = in.readInt();
        this.rule = in.readString();
        this.alipay = in.readParcelable(PayBean.class.getClassLoader());
        this.apple = in.readParcelable(PayBean.class.getClassLoader());
        this.wechat = in.readParcelable(PayBean.class.getClassLoader());
        this.cash = in.readParcelable(CashBean.class.getClassLoader());
        this.labels = new ArrayList<Integer>();
        in.readList(this.labels, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<WalletConfigBean> CREATOR = new Parcelable.Creator<WalletConfigBean>() {
        @Override
        public WalletConfigBean createFromParcel(Parcel source) {
            return new WalletConfigBean(source);
        }

        @Override
        public WalletConfigBean[] newArray(int size) {
            return new WalletConfigBean[size];
        }
    };

    @Override
    public String toString() {
        return "WalletConfigBean{" +
                "ratio=" + ratio +
                ", rule='" + rule + '\'' +
                ", alipay=" + alipay +
                ", apple=" + apple +
                ", wechat=" + wechat +
                ", cash=" + cash +
                ", labels=" + labels +
                '}';
    }
}
