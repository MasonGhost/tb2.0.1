package com.zhiyicx.zhibolibrary.di.module;

import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.zhibolibrary.model.api.service.CommonService;
import com.zhiyicx.zhibolibrary.model.api.service.LiveService;
import com.zhiyicx.zhibolibrary.model.api.service.SearchService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;

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
    CommonService provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @Singleton
    @Provides
    LiveService provideLiveService(Retrofit retrofit) {
        return retrofit.create(LiveService.class);
    }

    @Singleton
    @Provides
    SearchService provideSearchService(Retrofit retrofit) {
        return retrofit.create(SearchService.class);
    }

    @Singleton
    @Provides
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }


    @Singleton
    @Provides
    ImService provideImService(WebSocket webSocket) {
        return new ImService(webSocket);
    }

    @Singleton
    @Provides
    ServiceManager provideServiceManager(
            CommonService commonService
            , LiveService liveService
            , SearchService searchService
            , UserService userService

            , ImService imService) {
        return new ServiceManager(commonService, liveService
                , searchService,  userService,imService);
    }


}
