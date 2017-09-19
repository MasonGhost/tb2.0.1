package com.zhiyicx.zhibosdk.model;


import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/1.
 */
public interface CloudApiModel {
    Observable<JsonObject> sendCloudApiRequest(Map<String,Object> map);
}
