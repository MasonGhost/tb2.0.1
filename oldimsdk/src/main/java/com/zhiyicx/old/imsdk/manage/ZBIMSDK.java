package com.zhiyicx.old.imsdk.manage;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.old.imsdk.service.SocketService;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.old.imsdk.manage
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
        LogUtils.debugInfo("SocketService","--------------startService");
        context.startService(new Intent(context, SocketService.class));//开启socket服务,用于im通讯
    }
}
