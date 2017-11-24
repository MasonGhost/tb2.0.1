package com.zhiyicx.zhibosdk.model.imp;


import com.google.gson.JsonObject;
import com.zhiyicx.zhibosdk.model.CloudApiModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.service.ZBCommonService;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;

import java.util.Map;

import rx.Observable;

/**
 * Created by jungle on 2016/4/1.
 */
public class CloudApiModelImpl implements CloudApiModel {


    private ZBCommonService mCommonService;

    public CloudApiModelImpl(ZBServiceManager manager) {
        this.mCommonService = manager.getCommonService();
    }

    @Override
    public Observable<JsonObject> sendCloudApiRequest(Map<String, Object> map) {
        return mCommonService.getCommonApi(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API,map);
    }

}