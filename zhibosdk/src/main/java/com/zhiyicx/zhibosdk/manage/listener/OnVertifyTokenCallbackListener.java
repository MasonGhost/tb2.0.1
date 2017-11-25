package com.zhiyicx.zhibosdk.manage.listener;

import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnVertifyTokenCallbackListener {

    void onSuccess(ZBApiConfig zbApiConfig);

    void onError(Throwable throwable);

    void onFial(String code, String message);

}