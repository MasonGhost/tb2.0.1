package com.zhiyicx.tspay;

import android.app.Activity;

import com.pingplusplus.android.Pingpp;

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
    public static final String CHANNEL_WXPAY = "wx";
    /**
     * 手机网页发起微信支付
     */
    public static final String CHANNEL_WXWAPPAY = "wx_wap";

    public static void pay(String payCredentials, Activity activity) {
        Pingpp.createPayment(activity, payCredentials);
    }
}
