package com.zhiyicx.zhibosdk.manage.soupport;

import android.content.Context;

/**
 * Created by jungle on 16/8/3.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ConfigSoupport {


    /**
     * 票据验证
     * @param context
     * @param ticket
     */
    void vertifyToken(Context context, String ticket);

}
