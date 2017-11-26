package com.zhiyicx.zhibosdk.model.api.service;


import com.zhiyicx.old.imsdk.service.ImService;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public class ZBServiceManager {
    private ZBCommonService mCommonService;
    private LiveService mLiveService;
    private VideoService mVideoService;
    private GoldService mGoldService;
    private ImService mImService;

    public ZBServiceManager(ZBCommonService commonService
            , LiveService liveService
            , VideoService videoService
            , GoldService goldService
            , ImService imService) {
        this.mCommonService = commonService;
        this.mLiveService = liveService;
        this.mVideoService = videoService;
        this.mGoldService = goldService;
        this.mImService = imService;
    }

    public ZBCommonService getCommonService() {
        return mCommonService;
    }

    public LiveService getLiveService() {
        return mLiveService;
    }

    public VideoService getVideoService() {
        return mVideoService;
    }


    public GoldService getGoldService() {
        return mGoldService;
    }

    public ImService getImService() {
        return mImService;
    }
}
