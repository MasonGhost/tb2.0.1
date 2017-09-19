package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.model.LivePlayModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.CommonService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import okhttp3.ResponseBody;
import rx.Observable;

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
    public Observable<BaseJson<UserInfo[]>> getUsidInfo(String user_id, String file,
                                                        String accessKey,
                                                        String secretKey) {
        return mUserService.getUsIdInfo(ZBLApi.API_GET_USID_INFO, user_id, file, accessKey, secretKey);
    }


    @Override
    public Observable<BaseJson<FollowInfo>> followUser(String action, String userId, String accessKey, String secretKey) {
        return mUserService.followUser(ZBLApi.API_USER_FOLLW,action, userId, accessKey, secretKey);
    }


}
