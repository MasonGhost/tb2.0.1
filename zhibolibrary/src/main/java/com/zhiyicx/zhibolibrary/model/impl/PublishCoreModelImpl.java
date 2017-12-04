package com.zhiyicx.zhibolibrary.model.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.old.imsdk.service.ImService;
import com.zhiyicx.zhibolibrary.model.PublishCoreModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.LiveService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;
import com.zhiyicx.zhibosdk.manage.ZBPlayClient;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.FormBody;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/5/11.
 */
public class PublishCoreModelImpl implements PublishCoreModel {
    private ImService mService;
    private UserService mUserService;
    private LiveService mLiveService;

    @Inject
    public PublishCoreModelImpl(ServiceManager manager) {//
        mService = manager.getImService();//初始化服务
        mUserService = manager.getUserService();
        mLiveService = manager.getLiveService();
    }


    @Override
    public void sendTextMsg(String text, boolean presenter) {





        if (presenter) {
            ZBStreamingClient.getInstance().sendTextMsg(text);
        } else {
            ZBPlayClient.getInstance().sendTextMsg(text);
        }
    }

    @Override
    public void sendGiftMessage(Map jsonstr, String gift_code, String count, final OnCommonCallbackListener l) {
        ZBPlayClient.getInstance().sendGiftMessage(jsonstr, gift_code, count, l);
    }

    @Override
    public void sendZan(int type) {
        ZBPlayClient.getInstance().sendZan(type);
    }

    @Override
    public void sendAttention() {
        ZBPlayClient.getInstance().sendAttention();
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
        if(filed!=null) {
            builder.add("filed", filed);
        }
        getPermissionData(usid, builder);
        FormBody formBody = builder.build();
        return mUserService.getUsIdInfobyFrom(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());

    }
    private void getPermissionData(String usid, FormBody.Builder builder) {
        builder.add("usid", usid);
//        PermissionData[] permissionDatas= ZhiboApplication.getPermissionDatas();
//        for (PermissionData data : permissionDatas) {
//            builder.add(data.auth_key, data.auth_value);
//        }
    }
    @Override
    public Observable<BaseJson<SearchResult[]>> getGiftRank(String usid, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("usid", usid);
        map.put("p", page);

        return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_GOLD_RANK_LIST, map).observeOn(Schedulers.io()).map(new Func1<JsonObject, BaseJson<SearchResult[]>>() {
            @Override
            public BaseJson<SearchResult[]> call(JsonObject jsonObject) {
                ApiList apilist = new Gson().fromJson(jsonObject, ApiList.class);
                BaseJson<SearchResult[]> base = new BaseJson<SearchResult[]>();
                base.code = apilist.code;
                base.message = apilist.message;
                base.data = apilist.data;
                return base;
            }
        });
    }

}
