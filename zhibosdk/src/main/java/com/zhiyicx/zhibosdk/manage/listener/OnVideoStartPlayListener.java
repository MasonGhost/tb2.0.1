package com.zhiyicx.zhibosdk.manage.listener;

/**
 * Created by jungle on 16/7/18.
 * com.zhiyicx.zhibosdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnVideoStartPlayListener {
    void onSuccess();

    void onFail(String code,String message);

    void onError(Throwable throwable);

    void onLiveEnd(String jsonstr, String uid);

}
