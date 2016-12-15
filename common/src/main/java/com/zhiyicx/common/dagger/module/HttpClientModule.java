package com.zhiyicx.common.dagger.module;

import android.app.Application;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhiyicx.common.net.intercept.GlobeHttpHandler;
import com.zhiyicx.common.net.intercept.RequestIntercept;
import com.zhiyicx.rxerrorhandler.RxErrorHandler;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

@Module
public class HttpClientModule {
    public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;//缓存文件最大值为10Mb
    private static final int TOME_OUT = 10;

    private HttpUrl mApiUrl;
    private GlobeHttpHandler mHandler;
    private Interceptor[] mInterceptors;
    private ResponseErroListener mErroListener;

    /**
     * 设置 baseurl
     *
     * @param buidler
     */
    private HttpClientModule(Buidler buidler) {
        this.mApiUrl = buidler.apiUrl;
        this.mHandler = buidler.handler;
        this.mInterceptors = buidler.interceptors;
        this.mErroListener = buidler.responseErroListener;
    }

    public static Buidler buidler() {
        return new Buidler();
    }

    /**
     * 提供OkhttpClient
     *
     * @param cache     缓存
     * @param intercept 拦截器
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(Cache cache, Interceptor intercept) {
        final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        return configureClient(okHttpClient, cache, intercept);
    }

    /**
     * 提供 retrofit
     *
     * @param client
     * @param httpUrl
     * @return
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, HttpUrl httpUrl) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        return configureRetrofit(builder, client, httpUrl);
    }

    /**
     * 提供 baseUrl
     *
     * @return
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return mApiUrl;
    }

    /**
     * 提供文件缓存器，设置缓存路径和大小
     *
     * @param cacheFile
     * @return
     */
    @Singleton
    @Provides
    Cache provideCache(File cacheFile) {
        return new Cache(cacheFile, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }

    /**
     * 提供缓存地址
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return DataHelper.getCacheFile(application);
    }

    /**
     * 打印请求信息的拦截器
     *
     * @return
     */
    @Singleton
    @Provides
    Interceptor provideIntercept() {
        return new RequestIntercept(mHandler);
    }

    /**
     * 提供 RXCache 客户端
     *
     * @param cacheDir 缓存路径
     * @return
     */
    @Singleton
    @Provides
    RxCache provideRxCache(File cacheDir) {
        return new RxCache
                .Builder()
                .persistence(cacheDir, new GsonSpeaker());
    }


    /**
     * 提供处理 Rxjava 错误的管理器
     *
     * @return
     */
    @Singleton
    @Provides
    RxErrorHandler proRxErrorHandler(Application application) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErroListener(mErroListener)
                .build();
    }

    /**
     * 提供权限管理类,用于请求权限,适配 6.0 的权限管理
     *
     * @param application
     * @return
     */
    @Singleton
    @Provides
    RxPermissions provideRxPermissions(Application application) {
        return RxPermissions.getInstance(application);
    }


    /**
     * 配置retrofit
     *
     * @param builder
     * @param client
     * @param httpUrl
     * @return
     */
    private Retrofit configureRetrofit(Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl) {
        return builder
                .baseUrl(httpUrl)// 域名
                .client(client)// 设置 okhttp
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用 rxjava
                .addConverterFactory(GsonConverterFactory.create())// 使用 Gson
                .build();
    }

    /**
     * 配置 okhttpclient
     *
     * @param okHttpClient
     * @return
     */
    private OkHttpClient configureClient(OkHttpClient.Builder okHttpClient, Cache cache, Interceptor intercept) {
        OkHttpClient.Builder builder = okHttpClient
                .connectTimeout(TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(TOME_OUT, TimeUnit.SECONDS)
                .cache(cache)//设置缓存
                .addNetworkInterceptor(intercept);
        if (mInterceptors != null && mInterceptors.length > 0) {// 如果外部提供了 interceptor 的数组则遍历添加
            for (Interceptor interceptor : mInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder
                .build();
    }


    public static final class Buidler {
        private HttpUrl apiUrl = HttpUrl.parse("https://api.github.com/");
        private GlobeHttpHandler handler;
        private Interceptor[] interceptors;
        private ResponseErroListener responseErroListener;

        private Buidler() {
        }

        /**
         *
         * @param baseurl  基础 url
         * @return
         */
        public Buidler baseurl(String baseurl) {
            if (TextUtils.isEmpty(baseurl)) {
                throw new IllegalArgumentException("baseurl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseurl);
            return this;
        }

        /**
         * 用来处理http响应结果
         *
         * @param handler
         * @return
         */
        public Buidler globeHttpHandler(GlobeHttpHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 动态添加任意个 interceptor
         *
         * @param interceptors
         * @return
         */
        public Buidler interceptors(Interceptor[] interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        /**
         * 处理所有 Rxjava 的 onError 逻辑
         *
         * @param listener
         * @return
         */
        public Buidler responseErroListener(ResponseErroListener listener) {
            this.responseErroListener = listener;
            return this;
        }


        public HttpClientModule build() {
            if (apiUrl == null) {
                throw new IllegalStateException("baseurl is required");
            }
            return new HttpClientModule(this);
        }


    }

}
