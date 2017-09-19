package com.zhiyicx.zhibosdk.di.module;


import android.app.Application;
import android.content.Context;

import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.imsdk.core.autobahn.WebSocketConnection;
import com.zhiyicx.zhibosdk.model.api.RequestIntercept;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.utils.ZBDataHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.BaseUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class ClientModule {
    private static final int TOME_OUT = 10;
    public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 12 * 1024 * 1024;//缓存文件最大值为10Mb
    private final Application application;
    private int tryGetApiTimes =0;

    public ClientModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Singleton
    @Provides
    OkHttpClient provideClient() {
        final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        return configureClient(okHttpClient);
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        return configureRetrofit(builder, client);
    }

    @Singleton
    @Provides
    WebSocket provideWebSocketClient() {
        return new WebSocketConnection();
    }


    /**
     * @param builder
     * @param client
     * @return
     */
    private Retrofit configureRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder
                .baseUrl(new BaseUrl() {
                    @Override
                    public HttpUrl url() {
                        return HttpUrl.parse(ZBApi.USENOW_DOMAIN);
                    }
                })//域名
                .client(client)//设置okhttp
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create())//使用Gson
                .build();
    }

    /**
     * 配置okhttpclient
     *
     * @param okHttpClient
     * @return
     */
    private OkHttpClient configureClient(OkHttpClient.Builder okHttpClient) {
        return okHttpClient
                .connectTimeout(TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(TOME_OUT, TimeUnit.SECONDS)
                .cache(new Cache(ZBDataHelper.getCacheFile(application), HTTP_RESPONSE_DISK_CACHE_MAX_SIZE))//设置缓存路径和大小
                .addNetworkInterceptor(new RequestIntercept())
                .build();
    }

}
