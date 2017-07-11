package com.zhiyicx.thinksnsplus.modules.wallet;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/23
 * @Contact master.jungle68@gmail.com
 */

public enum PayType {
    ALIPAY("支付宝"),
    WX("微信");

    public String value;

    PayType(String value)

    {
        this.value = value;
    }

    public String getAcount() {
        return value;
    }
}
