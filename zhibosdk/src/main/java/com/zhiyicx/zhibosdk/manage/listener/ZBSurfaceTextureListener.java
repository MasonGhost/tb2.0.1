package com.zhiyicx.zhibosdk.manage.listener;

/**
 * Created by jungle on 16/8/18.
 * com.zhiyicx.zhibosdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ZBSurfaceTextureListener {

    void onSurfaceCreated();

    void onSurfaceChanged(int var1, int var2);

    void onSurfaceDestroyed();

    int onDrawFrame(int var1, int var2, int var3, float[] var4);
}
