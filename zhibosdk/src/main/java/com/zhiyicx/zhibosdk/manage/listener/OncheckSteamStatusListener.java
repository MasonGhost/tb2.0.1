package com.zhiyicx.zhibosdk.manage.listener;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OncheckSteamStatusListener {
    void onStartCheck();

    void onSuccess();

    void onError(Throwable throwable);

    void onFial(String code,String message);

    /**
     * 被禁播了
     */
    void onDisable(String time);
}
