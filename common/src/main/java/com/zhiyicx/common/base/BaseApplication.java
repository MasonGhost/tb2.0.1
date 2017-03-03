package com.zhiyicx.common.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;

import java.util.Set;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import solid.ren.skinlibrary.base.SkinBaseApplication;

/**
 * @Describe Applicaiton 基类
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BaseApplication extends SkinBaseApplication {
    protected final String TAG = this.getClass().getSimpleName();

    private static BaseApplication mApplication;
    private HttpClientModule mHttpClientModule;
    private AppModule mAppModule;

    private RefWatcher mRefWatcher;//leakCanary观察器

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        /**
         * leakCanary 内存泄露检查
         */
        installLeakCanary();
        /**
         * 日志初始化
         */
        LogUtils.init();
        mApplication = this;
        this.mHttpClientModule = HttpClientModule// 用于提供 okhttp 和 retrofit 的单列
                .buidler()
                .baseurl(getBaseUrl())
                .globeHttpHandler(getHttpHandler())
                .interceptors(getInterceptors())
                .responseErroListener(getResponseErroListener())
                .sslSocketFactory(getSSLSocketFactory())
                .build();
        this.mAppModule = new AppModule(this);// 提供 application

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 提供基础 url 给 retrofit
     *
     * @return
     */
    protected abstract String getBaseUrl();

    protected abstract ImageModule getImageModule();

    /**
     * 安装 leakCanary 检测内存泄露
     */
    protected void installLeakCanary() {
        this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    /**
     * 获得 leakCanary 观察器
     *
     * @param context
     * @return
     */
    public RefWatcher getRefWatcher(Context context) {

        return this.mRefWatcher;
    }

    public HttpClientModule getHttpClientModule() {
        return mHttpClientModule;
    }

    public AppModule getAppModule() {
        return mAppModule;
    }


    /**
     * 这里可以提供一个全局处理 http 响应结果的处理类,
     * 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如 token 超时,重新获取
     * 默认不实现,如果有需求可以重写此方法
     *
     * @return
     */
    protected RequestInterceptListener getHttpHandler() {
        return null;
    }

    /**
     * 用来提供 interceptor,如果要提供额外的 interceptor 可以让子 application 实现此方法
     *
     * @return
     */
    protected Set<Interceptor> getInterceptors() {
        return null;
    }


    /**
     * 用来提供处理所有错误的监听
     * 如果要使用 ErrorHandleSubscriber (默认实现 Subscriber 的 onError 方法)
     * 则让子 application 重写此方法
     *
     * @return
     */
    protected ResponseErroListener getResponseErroListener() {
        return new ResponseErroListener() {

            @Override
            public void handleResponseError(Context context, Throwable throwable) {

            }
        };
    }

    /**
     * 提供SSlFactory
     */
    protected SSLSocketFactory getSSLSocketFactory() {
        return null;
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }


}
