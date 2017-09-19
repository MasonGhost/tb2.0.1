package com.zhiyicx.zhibosdk.di.module;

import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.zhibosdk.model.api.service.ZBCommonService;
import com.zhiyicx.zhibosdk.model.api.service.GoldService;
import com.zhiyicx.zhibosdk.model.api.service.LiveService;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.api.service.VideoService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


/**
 * Created by zhiyicx on 2016/3/30.
 */
@Module
public class ServiceModule {

    @Singleton
    @Provides
    ZBCommonService provideCommonService(Retrofit retrofit) {
        return retrofit.create(ZBCommonService.class);
    }

    @Singleton
    @Provides
    LiveService provideLiveService(Retrofit retrofit) {
        return retrofit.create(LiveService.class);
    }



    @Singleton
    @Provides
    VideoService provideVideoService(Retrofit retrofit) {
        return retrofit.create(VideoService.class);
    }

    @Singleton
    @Provides
    GoldService provideGoldService(Retrofit retrofit) {
        return retrofit.create(GoldService.class);
    }

    @Singleton
    @Provides
    ImService provideImService(WebSocket webSocket) {
        return new ImService(webSocket);
    }

    @Singleton
    @Provides
    ZBServiceManager provideServiceManager(
            ZBCommonService commonService
            , LiveService liveService
            , VideoService videoService
            , GoldService goldService
            , ImService imService) {
        return new ZBServiceManager(commonService, liveService, videoService, goldService,imService);
    }


}
