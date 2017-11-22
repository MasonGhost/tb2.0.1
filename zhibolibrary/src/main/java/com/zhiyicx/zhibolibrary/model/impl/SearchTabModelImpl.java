package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.SearchTabModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.SearchService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.SearchJson;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;

import okhttp3.FormBody;
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
        PermissionData[] permissionDatas= ZhiboApplication.getPermissionDatas();
        for (PermissionData data : permissionDatas) {
            builder.add(data.auth_key, data.auth_value);
        }
        FormBody formBody = builder.build();
        return mUserService.getUsIdInfobyFrom(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());

    }

}
