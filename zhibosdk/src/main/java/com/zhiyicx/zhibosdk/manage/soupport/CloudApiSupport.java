package com.zhiyicx.zhibosdk.manage.soupport;

import com.google.gson.JsonObject;
import com.zhiyicx.zhibosdk.manage.listener.ZBCloudApiCallback;

import java.util.Map;

import rx.Observable;

/**
 * Created by jungle on 16/7/19.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface CloudApiSupport{

    /**
     * 直播云api
     *
     * @param map
     * @param callback
     */
    void sendZBCloudApiRequest(String api,Map<String, Object> map, final ZBCloudApiCallback callback);

    /**
     * 直播云api
     * @param map
     */
    Observable<JsonObject> sendCloudApiRequestForRx(String api, Map<String, Object> map);
}