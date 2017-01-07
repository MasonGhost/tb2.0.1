package com.zhiyicx.thinksnsplus.base;


import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;

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
    CommonClient provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonClient.class);
    }

    @Singleton
    @Provides
    LoginClient provideLoginClient(Retrofit retrofit) {
        return retrofit.create(LoginClient.class);
    }

}
