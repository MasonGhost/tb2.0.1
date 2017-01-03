package com.zhiyicx.imsdk.manage;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.imsdk.service.SocketService;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBIMSDK {
    public ZBIMSDK() {
    }
    public static void init(Context context) {
        startImService(context);
    }

    private static void startImService(Context context) {
        context.startService(new Intent(context, SocketService.class));//开启socket服务,用于im通讯
    }
}
