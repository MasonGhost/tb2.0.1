package com.zhiyicx.zhibosdk.manage.listener;

import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnCloseStatusListener {

    void onSuccess(ZBEndStreamJson endStreamJson);

    void onError(Throwable throwable);

    void onFial(String code, String message);

}
