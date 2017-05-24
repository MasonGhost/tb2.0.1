package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsDetailBean extends BaseListBean{
    private long time;
    private String account;
    private String money;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
