package com.zhiyicx.zhibolibrary.model.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.GoldRankModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/24.
 */
public class GoldRankModeImpl implements GoldRankModel {
    private UserService mUserService;

    public GoldRankModeImpl(ServiceManager manager) {
        this.mUserService = manager.getUserService();
    }

    @Override
    public Observable<BaseJson<SearchResult[]>> getRanking(String usid, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("usid", usid);
        map.put("p", page);

        return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_GOLD_RANK_LIST, map).observeOn(Schedulers.io()).map(new Func1<JsonObject, BaseJson<SearchResult[]>>() {
            @Override
            public BaseJson<SearchResult[]> call(JsonObject jsonObject) {
                ApiList apilist = new Gson().fromJson(jsonObject, ApiList.class);
                BaseJson<SearchResult[]> base = new BaseJson<SearchResult[]>();
                base.code=apilist.code;
                base.message=apilist.message;
                base.data = apilist.data;
                return base;
            }
        });
    }
    /**
     * 通过uisd获取用户信息
     *
     * @param usid
     * @return
     */

    @Override
    public Observable<BaseJson<UserInfo[]>> getUsidInfo(final String usid, String filed ) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_GET_USER_INFO);
        builder.add("usid", usid);
        builder.add("filed", filed);
//        PermissionData[] permissionDatas= ZhiboApplication.getPermissionDatas();
//        for (PermissionData data : permissionDatas) {
//            builder.add(data.auth_key, data.auth_value);
//        }
        FormBody formBody = builder.build();
        return mUserService.getUsIdInfobyFrom(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());

    }
}
