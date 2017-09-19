package com.zhiyicx.zhibosdk.manage.listener;

import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;

/**
 * Created by jungle on 16/7/11.
 * com.zhiyicx.zhibosdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public interface ZBCloudApiTCallback<T> {

    void onResponse(ZBBaseJson<T> response);

    void onError(Throwable throwable);
}
