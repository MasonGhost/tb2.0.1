package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.model.UserHomeModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;

import rx.Observable;

/**
 * Created by jess on 16/4/23.
 */
public class UserHomeModelImpl implements UserHomeModel {
    private UserService mService;

    public UserHomeModelImpl(ServiceManager manager) {
        this.mService = manager.getUserService();
    }


    @Override
    public Observable<BaseJson<FollowInfo>> followUser(String action, String userId, String accessKey, String secretKey) {
        return mService.followUser(ZBLApi.API_USER_FOLLW,action, userId, accessKey, secretKey);
    }
}
