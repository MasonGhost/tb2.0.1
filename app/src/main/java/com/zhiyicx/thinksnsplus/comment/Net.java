package com.zhiyicx.thinksnsplus.comment;

import android.text.TextUtils;

import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @Author Jliuer
 * @Date 2017/05/03/14:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class Net {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;// 缓存文件最大值为 10Mb
    private static final String DEFAULT_BASEURL = "https://api.github.com/";
    private static final int CONNECTED_TOME_OUT = 15;
    private static final int WRITE_TOME_OUT = 15;
    private static final int READE_TOME_OUT = 15;

    private HttpUrl mApiUrl;
    private RequestInterceptListener mHandler;
    private Set<Interceptor> mInterceptorSet;
    private ResponseErroListener mErroListener;
    private SSLSocketFactory mSSLSocketFactory;// 配置安全证书


    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder okhttpBuilder;

    private OkHttpClient configureClient(OkHttpClient.Builder okHttpClient, Cache cache, Interceptor intercept) {

        OkHttpClient.Builder builder = okHttpClient
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECTED_TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(READE_TOME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TOME_OUT, TimeUnit.SECONDS)
                .cache(cache)//设置缓存
                .addNetworkInterceptor(intercept);
        if (mSSLSocketFactory != null) {
            builder.sslSocketFactory(mSSLSocketFactory);
        }
        if (mInterceptorSet != null) {// 如果外部提供了 interceptor 则遍历添加
            for (Interceptor interceptor : mInterceptorSet) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder
                .build();
    }

    private Retrofit configureRetrofit(Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl) {

        return builder
                .baseUrl(httpUrl)// 域名
                .client(client)// 设置 okhttp
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用 rxjava
                .addConverterFactory(ScalarsConverterFactory.create())// 使用Scalars
                .addConverterFactory(GsonConverterFactory.create())// 使用 Gson
                .build();
    }

    public Net(Buidler buidler) {
        this.mApiUrl = buidler.apiUrl;
        this.mHandler = buidler.handler;
        this.mInterceptorSet = buidler.mInterceptorSet;
        this.mErroListener = buidler.responseErroListener;
        this.mSSLSocketFactory = buidler.mSSLSocketFactory;
    }

    public Buidler newBuidler() {
        return new Buidler(this);
    }

    public static final class Buidler {

        private HttpUrl apiUrl = HttpUrl.parse(DEFAULT_BASEURL);
        private RequestInterceptListener handler;
        private Set<Interceptor> mInterceptorSet;
        private ResponseErroListener responseErroListener;
        private SSLSocketFactory mSSLSocketFactory;

        public Buidler() {

        }

        public Buidler(Net net) {
            this.apiUrl = net.mApiUrl;
            this.handler = net.mHandler;
            this.mInterceptorSet = net.mInterceptorSet;
            this.responseErroListener = net.mErroListener;
            this.mSSLSocketFactory = net.mSSLSocketFactory;
        }

        /**
         * @param baseurl 基础 url
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
        public Buidler globeHttpHandler(RequestInterceptListener handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 动态添加任意个 interceptor
         *
         * @param interceptorSet
         * @return
         */
        public Buidler interceptors(Set<Interceptor> interceptorSet) {
            this.mInterceptorSet = interceptorSet;
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

        /**
         * 获取证书id
         *
         * @return
         */
        public Buidler sslSocketFactory(SSLSocketFactory mSSLSocketFactory) {
            this.mSSLSocketFactory = mSSLSocketFactory;
            return this;
        }

        public Net build() {
            if (apiUrl == null) {
                throw new IllegalStateException("baseurl is required");
            }
            return new Net(this);
        }
    }

}
