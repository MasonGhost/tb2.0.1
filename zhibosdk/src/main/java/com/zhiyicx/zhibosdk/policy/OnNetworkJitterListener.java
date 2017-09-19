package com.zhiyicx.zhibosdk.policy;

/**
 * Created by jess on 16/5/25.
 */
public interface OnNetworkJitterListener {

    void onNetworkJitter();
    //当前为数据流量
    void onNetInData();


}
