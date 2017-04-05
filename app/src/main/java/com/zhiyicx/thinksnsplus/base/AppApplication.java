package com.zhiyicx.thinksnsplus.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.antfortune.freeline.FreelineCore;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhiyicx.baseproject.base.TSApplication;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.net.HttpsSSLFactroyUtils;
import com.zhiyicx.common.net.intercept.CommonRequestIntercept;
import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.manage.ZBIMSDK;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_helper.MusicWindows;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.net.ssl.SSLSocketFactory;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class AppApplication extends TSApplication {
    @Inject
    AuthRepository mAuthRepository;
    private AlertDialog alertDialog; // token 过期弹框
    private static AuthBean mCurrentLoginAuth; //当前登录用户的信息
    private static HttpProxyCacheServer mMediaProxyCacheServer;
    public static List<String> sOverRead = new ArrayList<>();
    private static MusicWindows sMusicWindows;

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
        initComponent();
        if (mAuthRepository.getComponentStatusLocal().isIm()) { // 是否安装了 IM
            ZBIMSDK.init(getContext());
        }
        BackgroundTaskManager.getInstance(getContext()).startBackgroundTask();// 开启后台任务
        // 极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        MobclickAgent.setDebugMode( true );
        System.out.println("getPackageName() = " + getPackageName());
    }

    /**
     * 增加统一请求头
     *
     * @return
     */
    @Override
    protected Set<Interceptor> getInterceptors() {
        Map<String, String> params = new HashMap<>();//统一请求头数据
        Set<Interceptor> set = new HashSet<>();
        set.add(new CommonRequestIntercept(params));
        return set;
    }

    /**
     * 这里可以提供一个全局处理http响应结果的处理类,
     * 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
     * 默认不实现,如果有需求可以重写此方法
     *
     * @return
     */
    @Override
    public RequestInterceptListener getHttpHandler() {
        return new RequestInterceptListener() {
            @Override
            public Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response
                    response) {
                // 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                // token过期，调到登陆页面重新请求token,
                LogUtils.i("baseJson-->" + httpResult);
                BaseJson baseJson = new Gson().fromJson(httpResult, BaseJson.class);
                if (baseJson != null && (baseJson.getCode() == ErrorCodeConfig.TOKEN_EXPIERD
                        || baseJson.getCode() == ErrorCodeConfig.NEED_RELOGIN
                        || baseJson.getCode() == ErrorCodeConfig.OTHER_DEVICE_LOGIN
                        || baseJson.getCode() == ErrorCodeConfig.USER_AUTH_FAIL
                        || baseJson.getCode() == ErrorCodeConfig.USER_NOT_FOUND
                        || baseJson.getCode() == ErrorCodeConfig.TOKEN_NOT_EXIST)) {
                    // 跳到登陆页面，销毁之前的所有页面,添加弹框处理提示
                    // 通过rxjava在主线程处理弹框
                    Observable.empty()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnCompleted(new Action0() {
                                @Override
                                public void call() {
                                    if (alertDialog == null) {
                                        alertDialog = new AlertDialog.Builder(ActivityHandler
                                                .getInstance().currentActivity())
                                                .setTitle(R.string.token_expiers)
                                                .setPositiveButton(R.string.sure, new
                                                        DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface
                                                                                        dialogInterface,
                                                                                int i) {
                                                                // TODO: 2017/2/8  清理登录信息 token 信息
                                                                mAuthRepository.clearAuthBean();
                                                                BackgroundTaskManager.getInstance
                                                                        (getContext())
                                                                        .closeBackgroundTask();//
                                                                // 关闭后台任务
                                                                Intent intent = new Intent
                                                                        (getContext(),
                                                                                LoginActivity
                                                                                        .class);
                                                                ActivityHandler.getInstance()
                                                                        .currentActivity()
                                                                        .startActivity
                                                                                (intent);
                                                                alertDialog.dismiss();
                                                            }
                                                        })
                                                .create();
                                    }
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.show();

                                }
                            })
                            .doOnError(new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            })
                            .subscribe();
                }
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则返回request
                AuthBean authBean = mAuthRepository.getAuthBean();
                if (authBean != null) {
                    return chain.request().newBuilder().header("ACCESS-TOKEN", authBean.getToken())
                            .build();
                }

                return request;
            }
        };
    }

    /**
     * rx 对 mErrorHandler 错误统一回调处理
     *
     * @return
     */
    @Override
    protected ResponseErroListener getResponseErroListener() {
        return new ResponseErroListener() {
            @Override
            public void handleResponseError(Context context, Throwable e) {
                LogUtils.d(TAG, "------------>" + e.getMessage());
            }
        };
    }

    /**
     * 初始化Component
     */
    public void initComponent() {
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())// baseApplication 提供
                .httpClientModule(getHttpClientModule())// baseApplication 提供
                .imageModule(getImageModule())// // 图片加载框架
                .serviceModule(getServiceModule())// 需自行创建
                .cacheModule(getCacheModule())// 需自行创建
                .build();
        AppComponentHolder.setAppComponent(appComponent);
        appComponent.inject(this);
    }

    @NonNull
    protected CacheModule getCacheModule() {
        return new CacheModule();
    }

    @NonNull
    protected ServiceModule getServiceModule() {
        return new ServiceModule();
    }

    @Override
    protected SSLSocketFactory getSSLSocketFactory() {
        int[] a = {R.raw.plus};
        return HttpsSSLFactroyUtils.getSSLSocketFactory(this, a);
    }

    /**
     * 将AppComponent返回出去,供其它地方使用
     *
     * @return
     */
    public static class AppComponentHolder {
        private static AppComponent sAppComponent;

        public static void setAppComponent(AppComponent appComponent) {
            sAppComponent = appComponent;
        }

        public static AppComponent getAppComponent() {
            return sAppComponent;
        }

    }

    /**
     * 在启动页面尝试刷新Token,同时需要刷新im的token
     */
    private void attemptToRefreshToken() {
        mAuthRepository.refreshToken();
    }

    public static AuthBean getmCurrentLoginAuth() {
        return mCurrentLoginAuth;
    }

    public static void setmCurrentLoginAuth(AuthBean mCurrentLoginAuth) {
        AppApplication.mCurrentLoginAuth = mCurrentLoginAuth;
    }

    public static HttpProxyCacheServer getProxy() {
        return AppApplication.mMediaProxyCacheServer == null ? (AppApplication
                .mMediaProxyCacheServer = newProxy()) : AppApplication.mMediaProxyCacheServer;
    }

    public static MusicWindows getMusicWindows() {
        return sMusicWindows == null ? new MusicWindows(BaseApplication.getContext()) :
                sMusicWindows;
    }

    private static HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(BaseApplication.getContext())
                .cacheDirectory(new File(FileUtils.getCacheFile(BaseApplication.getContext(), false)// liuchao 2017.3.27修改获取缓存历经
                        , "/media"))
                .maxCacheFilesCount(100)
                .build();
    }

}
