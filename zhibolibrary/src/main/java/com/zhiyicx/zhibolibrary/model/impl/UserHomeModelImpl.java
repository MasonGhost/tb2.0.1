package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.UserHomeModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import okhttp3.FormBody;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/23.
 */
public class UserHomeModelImpl implements UserHomeModel {
    private UserService mUserService;

    public UserHomeModelImpl(ServiceManager manager) {
        this.mUserService = manager.getUserService();
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
        builder.add("filed", filed);
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

}
