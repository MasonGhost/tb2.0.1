package com.zhiyicx.zhibolibrary.model.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.zhibolibrary.model.EndStreamModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/4/5.
 */
public class EndStreamModelImpl implements EndStreamModel {
    private UserService mUserService;

    public EndStreamModelImpl(ServiceManager manager) {
        this.mUserService = manager.getUserService();
    }

    /**
     * 获取直播列表
     * @param accesskey
     * @param secretkey
     * @param order
     * @param page
     * @return
     */
    @Override
    public Observable<ApiList> getList(String accesskey, String secretkey, String order, int page) {
        Map<String,Object> map=new HashMap<>();
        map.put("p",page);
        map.put("order",order);

        return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_LIVE_LIST,map).subscribeOn(Schedulers.io()).map(new Func1<JsonObject, ApiList>() {
            @Override
            public ApiList call(JsonObject jsonObject) {
                return new Gson().fromJson(jsonObject,ApiList.class);
            }
        });
    }


    /**
     * 关注
     * @param action
     * @param userId
     * @param accessKey
     * @param secretKey
     * @return
     */
    @Override
    public Observable<BaseJson<FollowInfo>> followUser(String action, String userId, String accessKey, String secretKey) {
        return mUserService.followUser(ZBLApi.API_USER_FOLLW,action, userId, accessKey, secretKey);
    }
}
