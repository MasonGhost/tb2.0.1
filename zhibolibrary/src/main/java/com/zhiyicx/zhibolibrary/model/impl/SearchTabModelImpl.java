package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.SearchTabModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.SearchService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchJson;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class SearchTabModelImpl implements SearchTabModel {
    private SearchService mSearchService;
    private UserService mUserService;
    public SearchTabModelImpl(ServiceManager serviceManager) {
        this.mSearchService = serviceManager.getSearchService();
        this.mUserService=serviceManager.getUserService();
    }

    /**
     * 查询用户
     * @param accessKey
     * @param secretKey
     * @param keyword
     * @param page
     * @return
     */
    @Override
    public Observable<SearchJson> Search(String accessKey, String secretKey, String keyword, int page) {
        return mSearchService.Search(ZBLApi.API_USER_SEARCH,accessKey, secretKey, keyword, page);
    }
    @Override
    public Observable<BaseJson<UserInfo[]>> getUsidInfo(final String userId, String filed) {

        return mUserService.getUsIdInfo(ZBLApi.API_GET_USID_INFO, userId, "", ZhiboApplication.userInfo.auth_accesskey, ZhiboApplication.userInfo.auth_secretkey).subscribeOn(Schedulers.io());

    }

}
