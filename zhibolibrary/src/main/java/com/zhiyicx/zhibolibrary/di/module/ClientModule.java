package com.zhiyicx.zhibolibrary.di.module;

import com.zhiyicx.imsdk.core.autobahn.WebSocket;
import com.zhiyicx.imsdk.core.autobahn.WebSocketConnection;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.RequestIntercept;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Module
public class ClientModule {
    private static final int TOME_OUT = 10;
    public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;//缓存文件最大值为10Mb

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
                .baseUrl(ZBLApi.BASE_URL)//域名
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
                .cache(new Cache(DataHelper.getCacheFile(UiUtils.getContext()), HTTP_RESPONSE_DISK_CACHE_MAX_SIZE))//设置缓存路径和大小
                .addNetworkInterceptor(new RequestIntercept())
                .build();
    }


}
