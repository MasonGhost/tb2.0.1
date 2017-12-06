package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

/**
 * @Author Jliuer
 * @Date 2017/06/02/9:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
public enum WithdrawType {

    ALIWITHDRAW("alipay"),

    WXWITHDRAW("wechat");

    public String type;

    WithdrawType(String type) {
        this.type = type;
    }
}
