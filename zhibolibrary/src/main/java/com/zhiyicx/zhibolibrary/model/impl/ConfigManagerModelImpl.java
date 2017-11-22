package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.ConfigManagerModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import okhttp3.FormBody;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/23.
 */
public class ConfigManagerModelImpl implements ConfigManagerModel {
    private UserService mService;

    public ConfigManagerModelImpl(ServiceManager manager) {
        this.mService = manager.getUserService();
    }


    /**
     * 通过uisd获取用户信息
     *
     * @return
     */
    @Override
    public Observable<BaseJson<UserInfo[]>> getUsidInfo(final String usid, String filed) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_GET_USER_INFO);
        builder.add("usid", usid);
        builder.add("filed", filed);
        PermissionData[] permissionDatas = ZhiboApplication.getPermissionDatas();
        for (PermissionData data : permissionDatas) {
            builder.add(data.auth_key, data.auth_value);
        }
        FormBody formBody = builder.build();
        return mService.getUsIdInfobyFrom(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());

    }

    @Override
    public Observable<BaseJson<PermissionData[]>> getPermissionData(String ticket) {
        return mService.getPermission(ZBLApi.CONFIG_BASE_DOMAIN, ZBLApi.API_USER_GET_AUTHBYTICKET, ticket);
    }
}
