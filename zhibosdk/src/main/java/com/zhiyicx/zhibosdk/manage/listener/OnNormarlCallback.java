package com.zhiyicx.zhibosdk.manage.listener;

/**
 * Created by jungle on 16/9/10.
 * com.zhiyicx.zhibosdk.manage.listener
 * zhibo_android
 * email:335891510@qq.com
 */
public interface OnNormarlCallback<T> {
    void onSuccess(T data);
    void onFail(String code,String message);
    void onError(Throwable throwable);
}
