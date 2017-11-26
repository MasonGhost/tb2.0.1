package com.zhiyicx.zhibolibrary.model.api.service;


import com.zhiyicx.old.imsdk.service.ImService;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public class ServiceManager {
    private CommonService mCommonService;
    private LiveService mLiveService;
    private SearchService mSearchService;
    private UserService mUserService;
    private ImService mImService;
    private GoldService mGoldService;

    public ServiceManager(CommonService commonService
            , LiveService liveService
            , SearchService searchService
            , UserService userService
            , ImService imService
            , GoldService goldService
    ) {
        this.mCommonService = commonService;
        this.mLiveService = liveService;
        this.mSearchService = searchService;
        this.mUserService = userService;
        this.mImService = imService;
        this.mGoldService=goldService;
    }

    public CommonService getCommonService() {
        return mCommonService;
    }

    public LiveService getLiveService() {
        return mLiveService;
    }


    public SearchService getSearchService() {
        return mSearchService;
    }


    public UserService getUserService() {
        return mUserService;
    }


    public ImService getImService() {
        return mImService;
    }
    public GoldService getGoldService(){return mGoldService;}
}
