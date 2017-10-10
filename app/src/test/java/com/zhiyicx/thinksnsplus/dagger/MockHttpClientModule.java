package com.zhiyicx.thinksnsplus.dagger;

import android.app.Application;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.rxerrorhandler.RxErrorHandler;

import java.io.File;

import io.rx_cache.internal.RxCache;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/29
 * @Contact master.jungle68@gmail.com
 */

public class MockHttpClientModule extends HttpClientModule {
    /**
     * 设置 baseurl
     *
     * @param buidler
     */
    public MockHttpClientModule(Buidler buidler) {
        super(buidler);
    }
    public MockHttpClientModule() {
        super(null);
    }
    @Override
    public OkHttpClient provideClient(Cache cache, Interceptor intercept) {
        return  mock(OkHttpClient.class);
    }

    @Override
    public Cache provideCache(File cacheFile) {
        return mock(Cache.class);
    }

    @Override
    public File provideCacheFile(Application application) {
        return mock(File.class);
    }

    @Override
    public HttpUrl provideBaseUrl() {
        return mock(HttpUrl.class);
    }

    @Override
    public Interceptor provideIntercept() {
        return mock(Interceptor.class);
    }

    @Override
    public Retrofit provideRetrofit(OkHttpClient client, HttpUrl httpUrl) {
        return mock(Retrofit.class);
    }

    @Override
    public RxCache provideRxCache(File cacheDir) {
            return mock(RxCache.class);
    }

    @Override
    public RxErrorHandler proRxErrorHandler(Application application) {
        return mock(RxErrorHandler.class);
    }

}



