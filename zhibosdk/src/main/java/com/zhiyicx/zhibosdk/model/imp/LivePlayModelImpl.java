package com.zhiyicx.zhibosdk.model.imp;


import com.zhiyicx.zhibosdk.model.LivePlayModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.service.GoldService;
import com.zhiyicx.zhibosdk.model.api.service.LiveService;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.api.service.VideoService;
import com.zhiyicx.zhibosdk.model.entity.ZBApiPlay;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;

import rx.Observable;

/**
 * Created by jungle on 2016/4/1.
 */
public class LivePlayModelImpl implements LivePlayModel {
    private LiveService mLiveService;
    private VideoService mVideoService;
    private GoldService mGoldService;

    public LivePlayModelImpl(ZBServiceManager manager) {
        this.mLiveService = manager.getLiveService();
        this.mVideoService = manager.getVideoService();
        this.mGoldService=manager.getGoldService();
    }

    /**
     * 获取直播地址
     * @param ak
     * @param uid
     * @param id
     * @return
     */
    @Override
    public Observable<ZBApiPlay> getPlayUrl(String ak, String uid, String id) {
        return mLiveService.getPlayUrl(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API,ZBApi.API_GET_PLAYURL,ak
                , uid, id);
    }

    /**
     * 获取回放地址
     * @param ak
     * @param vid
     * @return
     */
    @Override
    public Observable<ZBApiPlay> getVideoUrl(String ak, String vid) {
        return mVideoService.getVideoUrl(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API,ZBApi.API_GET_VIDEOURL,ak,vid);
    }

    /**
     * 送礼物

     * @param ak
     * @param usid
     * @param gift_code
     * @param count
     * @return
     */
    @Override
    public Observable<ZBBaseJson<String>> sendGift( String ak, String usid, String gift_code, String count) {
        return mGoldService.sendGift(ZBApi.USENOW_DOMAIN+ZBApi.BASE_API,ZBApi.API_SEND_GIFT,ak,usid,gift_code,count);
    }

    /**
     * 点赞

     * @param ak
     * @param usid
     * @param count
     * @return
     */
    @Override
    public Observable<ZBBaseJson<String>> sendZan( String ak, String usid, String count) {
        return mGoldService.sendZan(ZBApi.USENOW_DOMAIN+ZBApi.BASE_API,ZBApi.API_SEND_ZAN,ak,usid,count);
    }
}
