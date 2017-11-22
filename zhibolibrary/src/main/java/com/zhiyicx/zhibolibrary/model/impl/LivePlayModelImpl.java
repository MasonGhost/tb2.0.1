package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.LivePlayModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.CommonService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 2016/4/1.
 */
public class LivePlayModelImpl implements LivePlayModel {


    private CommonService mService;
    private UserService mUserService;

    public LivePlayModelImpl(ServiceManager manager) {
        this.mService = manager.getCommonService();//初始化请求服务
        this.mUserService=manager.getUserService();
    }

    @Override
    public Observable<ResponseBody> downloadFile(String fileUrl) {
        return mService.downloadFile(fileUrl);
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
        PermissionData[] permissionDatas= ZhiboApplication.getPermissionDatas();
        for (PermissionData data : permissionDatas) {
            builder.add(data.auth_key, data.auth_value);
        }
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
