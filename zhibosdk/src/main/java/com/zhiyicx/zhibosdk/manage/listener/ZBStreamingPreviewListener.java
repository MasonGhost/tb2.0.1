package com.zhiyicx.zhibosdk.manage.listener;

import android.hardware.Camera;

/**
 * Created by jungle on 16/8/18.
 * com.zhiyicx.zhibosdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ZBStreamingPreviewListener {
    boolean onPreviewFrame(byte[] var1, int var2, int var3);
}
