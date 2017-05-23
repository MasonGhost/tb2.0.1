package com.zhiyicx.thinksnsplus.modules.wallet;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/23
 * @Contact master.jungle68@gmail.com
 */

public enum PayType {
    ALIPAY(1),
    WX(2);

    public int value;

    PayType(int value)

    {
        this.value = value;
    }
}
