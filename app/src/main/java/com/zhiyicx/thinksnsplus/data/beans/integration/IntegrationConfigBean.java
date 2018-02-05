package com.zhiyicx.thinksnsplus.data.beans.integration;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.StringArrayConvert;

import org.greenrobot.greendao.annotation.Convert;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Describe 积分配置信息 doc @see{https://slimkit.github.io/plus-docs/v2/core/currency}
 * @Author Jungle68
 * @Date 2018/1/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationConfigBean implements Serializable {

    private static final long serialVersionUID = -1293476228114357406L;
    /**
     * recharge-ratio	int	兑换比例，人民币一分钱可兑换的积分数量
     * recharge-options	string	充值选项，人民币分单位
     * recharge-max	int	单笔最高充值额度
     * recharge-min	int	单笔最小充值额度
     * rule	string	积分规则
     * cash-min
     * cash-max
     */

    @SerializedName("recharge-ratio")
    private int rechargeratio;
    @SerializedName("recharge-options")
    private String rechargeoptions;
    @SerializedName("recharge-max")
    private long rechargemax;
    @SerializedName("recharge-min")
    private int rechargemin;
    @SerializedName("cash-max")
    private long cashmax;
    @SerializedName("cash-min")
    private int cashmin;

    private String rule;
    @SerializedName("cash-rule")
    private String cashrule;
    @SerializedName("recharge-rule")
    private String rechargerule;


    @Convert(converter = StringArrayConvert.class, columnType = String.class)
    private String[] cash;

    @Convert(converter = StringArrayConvert.class, columnType = String.class)
    @SerializedName("recharge-type")
    private String[] recharge_type;

    public int getRechargeratio() {
        return rechargeratio;
    }

    public void setRechargeratio(int rechargeratio) {
        this.rechargeratio = rechargeratio;
    }

    public String getRechargeoptions() {
        return rechargeoptions;
    }

    public void setRechargeoptions(String rechargeoptions) {
        this.rechargeoptions = rechargeoptions;
    }

    public long getRechargemax() {
        return rechargemax;
    }

    public void setRechargemax(long rechargemax) {
        this.rechargemax = rechargemax;
    }

    public int getRechargemin() {
        return rechargemin;
    }

    public void setRechargemin(int rechargemin) {
        this.rechargemin = rechargemin;
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

    public String[] getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(String[] recharge_type) {
        this.recharge_type = recharge_type;
    }

    public String getCashrule() {
        return cashrule;
    }

    public void setCashrule(String cashrule) {
        this.cashrule = cashrule;
    }

    public String getRechargerule() {
        return rechargerule;
    }

    public void setRechargerule(String rechargerule) {
        this.rechargerule = rechargerule;
    }

    public long getCashmax() {
        return cashmax;
    }

    public void setCashmax(long cashmax) {
        this.cashmax = cashmax;
    }

    public int getCashmin() {
        return cashmin;
    }

    public void setCashmin(int cashmin) {
        this.cashmin = cashmin;
    }

    @Override
    public String toString() {
        return "IntegrationConfigBean{" +
                "rechargeratio=" + rechargeratio +
                ", rechargeoptions='" + rechargeoptions + '\'' +
                ", rechargemax=" + rechargemax +
                ", rechargemin=" + rechargemin +
                ", cashmax=" + cashmax +
                ", cashmin=" + cashmin +
                ", rule='" + rule + '\'' +
                ", cashrule='" + cashrule + '\'' +
                ", rechargerule='" + rechargerule + '\'' +
                ", cash=" + Arrays.toString(cash) +
                ", recharge_type=" + Arrays.toString(recharge_type) +
                '}';
    }
}
