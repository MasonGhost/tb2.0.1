package com.zhiyicx.zhibosdk.manage.listener;

/**
 * 开启直播状态监听
 * Created by jess on 16/5/25.
 */
public interface OnLiveStartPlayListener {

    void onStartPre();

    void onStartReady();

    void onStartSuccess();

    void onStartFail();
}
