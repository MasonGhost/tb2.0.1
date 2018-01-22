package com.zhiyicx.thinksnsplus.data.beans.integration;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Describe 积分配置信息 doc @see{https://slimkit.github.io/plus-docs/v2/core/currency}
 * @Author Jungle68
 * @Date 2018/1/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationConfigBean implements Serializable{

    private static final long serialVersionUID = -1293476228114357406L;
    /**
     * recharge-ratio	int	兑换比例，人民币一分钱可兑换的积分数量
     * recharge-options	string	充值选项，人民币分单位
     * recharge-max	int	单笔最高充值额度
     * recharge-min	int	单笔最小充值额度
     * rule	string	积分规则
     */

    @SerializedName("recharge-ratio")
    private int rechargeratio;
    @SerializedName("recharge-options")
    private String rechargeoptions;
    @SerializedName("recharge-max")
    private long rechargemax;
    @SerializedName("recharge-min")
    private int rechargemin;
    private String rule;

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

    @Override
    public String toString() {
        return "IntegrationConfigBean{" +
                "rechargeratio=" + rechargeratio +
                ", rechargeoptions='" + rechargeoptions + '\'' +
                ", rechargemax=" + rechargemax +
                ", rechargemin=" + rechargemin +
                ", rule='" + rule + '\'' +
                '}';
    }
}
