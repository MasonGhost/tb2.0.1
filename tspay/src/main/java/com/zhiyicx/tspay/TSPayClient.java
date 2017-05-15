package com.zhiyicx.tspay;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/15
 * @Contact master.jungle68@gmail.com
 */

public class TSPayClient {
    /**
     * TS 支付初始化
     *
     * @param context
     * @param wxAppId
     */
    public static void init(Context context, String wxAppId) {
        initWxPay(context, wxAppId);
    }

    /**
     * 微信支付初始化
     *
     * @param context
     * @param wxAppId
     */
    private static void initWxPay(Context context, String wxAppId) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        msgApi.registerApp(wxAppId);
    }
}
