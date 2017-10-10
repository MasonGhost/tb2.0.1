package com.zhiyicx.tspay;

import android.app.Activity;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;

import com.pingplusplus.android.Pingpp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/15
 * @Contact master.jungle68@gmail.com
 */
public class TSPayClient {
    /**
     * 手机支付宝 APP 支付
     */
    public static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 手机支付宝扫码支付
     */
    public static final String CHANNEL_ALIQRPAY = "alipay_qr";
    /**
     * 手机网页发起支付宝支付
     */
    public static final String CHANNEL_ALIWAPPAY = "alipay_wap";
    /**
     * 微信 APP 支付
     */
    public static final String CHANNEL_WXPAY = "wechat";
    /**
     * 手机网页发起微信支付
     */
    public static final String CHANNEL_WXWAPPAY = "wx_wap";

    public static final ArrayMap<String, Integer> PAY_KEYS_TYPE;

    static {
        PAY_KEYS_TYPE = new ArrayMap<>();
        PAY_KEYS_TYPE.put(CHANNEL_ALIPAY, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_ALIQRPAY, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_ALIWAPPAY, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_WXPAY, R.string.wxpay);
        PAY_KEYS_TYPE.put(CHANNEL_WXWAPPAY, R.string.wxpay);
    }

    @RestrictTo(LIBRARY_GROUP)
    @StringDef({CHANNEL_ALIPAY, CHANNEL_ALIQRPAY, CHANNEL_ALIWAPPAY, CHANNEL_WXPAY, CHANNEL_WXWAPPAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayKey {
    }

    public static void pay(String payCredentials, Activity activity) {
        Pingpp.createPayment(activity, payCredentials);
    }
}
