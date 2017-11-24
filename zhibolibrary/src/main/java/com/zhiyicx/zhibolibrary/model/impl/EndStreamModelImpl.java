package com.zhiyicx.zhibolibrary.model.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.EndStreamModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
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

    @Override
    public Observable<BaseJson<FollowInfo>> followUser(String action, String usid) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_USER_FOLLW);
        builder.add("action",action);
        getPermissionData(usid, builder);
        FormBody formBody = builder.build();
        return mUserService.followUser(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());
    }

    private void getPermissionData(String usid, FormBody.Builder builder) {
        builder.add("usid", usid);
//        PermissionData[] permissionDatas= ZhiboApplication.getPermissionDatas();
//        for (PermissionData data : permissionDatas) {
//            builder.add(data.auth_key, data.auth_value);
//        }
    }

    /**
     * 通过uisd获取用户信息
     *
     * @return
     */

    @Override
    public Observable<BaseJson<UserInfo[]>> getUsidInfo(final String usid, String filed ) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_GET_USER_INFO);
        builder.add("filed", filed);
        getPermissionData(usid, builder);
        FormBody formBody = builder.build();
        return mUserService.getUsIdInfobyFrom(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());

    }
}
