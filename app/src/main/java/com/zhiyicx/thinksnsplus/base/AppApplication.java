package com.zhiyicx.thinksnsplus.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.antfortune.freeline.FreelineCore;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.umeng.analytics.MobclickAgent;
import com.zhiyicx.baseproject.base.TSApplication;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.utils.WindowUtils;
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
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.QueueManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;
import com.zhiyicx.tspay.TSPayClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    @Inject
    SystemRepository mSystemRepository;

    private AlertDialog alertDialog; // token 过期弹框
    private static AuthBean mCurrentLoginAuth; //当前登录用户的信息
    private static HttpProxyCacheServer mMediaProxyCacheServer;
    private static QueueManager sQueueManager;
    private static PlaybackManager sPlaybackManager;
    public static List<String> sOverRead = new ArrayList<>();
    public int mActivityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
        initComponent();
        // IM
        if (!mAuthRepository.isTourist() && !TextUtils.isEmpty(mSystemRepository.getBootstrappersInfoFromLocal().getIm_serve())) { // 不是游客并且安装了 IM
            LogUtils.d(TAG, "---------------start IM---------------------");
            ZBIMSDK.init(getContext());
        }
        BackgroundTaskManager.getInstance(getContext()).startBackgroundTask();// 开启后台任务
        // 极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        // 友盟
        MobclickAgent.setDebugMode(com.zhiyicx.thinksnsplus.BuildConfig.DEBUG);
        registerActivityCallBacks();
        // 支付
        TSPayClient.init(this, PayConfig.WX_APP_ID);

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
                BaseJson baseJson = null;
                try {
                    baseJson = new Gson().fromJson(httpResult, BaseJson.class);
                } catch (JsonSyntaxException e) {
                }

                String tipStr = null;
                if (baseJson != null) {
                    switch (baseJson.getCode()) {
                        case ErrorCodeConfig.TOKEN_EXPIERD:
                            tipStr = getString(R.string.code_1012);
                            break;
                        case ErrorCodeConfig.NEED_RELOGIN:
                            tipStr = getString(R.string.code_1013);
                            break;
                        case ErrorCodeConfig.NEED_NO_DEVICE:
                            tipStr = getString(R.string.code_1014);
                            break;
                        case ErrorCodeConfig.OTHER_DEVICE_LOGIN:
                            tipStr = getString(R.string.code_1015);
                            break;
                        case ErrorCodeConfig.TOKEN_NOT_EXIST:
                            tipStr = getString(R.string.code_1016);
                            break;
                        case ErrorCodeConfig.USER_AUTH_FAIL:
                            tipStr = getString(R.string.code_1099);
                            break;
                        default:
                    }
                    if (!TextUtils.isEmpty(tipStr)) {
                        handleAuthFail(tipStr);
                    }
                }
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的 requeat 如增加 header,不做操作则返回 request
                AuthBean authBean = mAuthRepository.getAuthBean();
                if (authBean != null) {
                    return chain.request().newBuilder()
                            .header("Accept", "application/json")
                            .header((request.url() + "").contains("v1") ? "ACCESS-TOKEN" : "Authorization", (request.url() + "").contains("v1") ? authBean.getToken() : " Bearer " + authBean.getToken())
                            .build();
                } else {
                    return chain.request().newBuilder()
                            .header("Accept", "application/json")
                            .build();
                }
            }
        };
    }

    /**
     * 认证失败弹框
     *
     * @param tipStr
     */
    private void handleAuthFail(final String tipStr) {
        // 跳到登陆页面，销毁之前的所有页面,添加弹框处理提示
        // 通过rxjava在主线程处理弹框
        Observable.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        if ((alertDialog != null && alertDialog.isShowing()) || ActivityHandler
                                .getInstance().currentActivity() instanceof LoginActivity) { // 认证失败，弹框去重
                            return;
                        }
                        alertDialog = new AlertDialog.Builder(ActivityHandler
                                .getInstance().currentActivity(), R.style.TSWarningAlertDialogStyle)
                                .setMessage(tipStr)
                                .setOnKeyListener(new DialogInterface.OnKeyListener() {

                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode,
                                                         KeyEvent event) {
                                        if (alertDialog.isShowing() && keyCode == KeyEvent.KEYCODE_BACK
                                                && event.getRepeatCount() == 0) {
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                                .setPositiveButton(R.string.sure, new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface
                                                                        dialogInterface,
                                                                int i) {
                                                // TODO: 2017/2/8  清理登录信息 token 信息
                                                mAuthRepository.clearAuthBean();
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
                        alertDialog.setCanceledOnTouchOutside(false);
                        try {
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
     * 获取我的用户 id ，default = -1;
     *
     * @return
     */
    public static int getMyUserIdWithdefault() {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        int userId;
        if (authBean == null) {
            userId = -1;
        } else {
            userId = authBean.getUser_id();
        }
        return userId;
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

    private static HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(BaseApplication.getContext())
                .cacheDirectory(new File(FileUtils.getCacheFile(BaseApplication.getContext(), false)// liuchao 2017.3.27修改获取缓存历经
                        , "/media"))
                .maxCacheFilesCount(100)
                .build();
    }

    public static QueueManager getmQueueManager() {
        return sQueueManager;
    }

    public static PlaybackManager getPlaybackManager() {
        return sPlaybackManager;
    }

    public static void setmQueueManager(QueueManager mQueueManager) {
        AppApplication.sQueueManager = mQueueManager;
    }

    public static void setPlaybackManager(PlaybackManager playbackManager) {
        AppApplication.sPlaybackManager = playbackManager;
    }

    private void registerActivityCallBacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                mActivityCount--;
                if (mActivityCount == 0) {// 切到后台
                    WindowUtils.hidePopupWindow();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivityCount++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity instanceof MusicPlayActivity || activity instanceof GalleryActivity) {
                    WindowUtils.hidePopupWindow();
                } else if (sPlaybackManager != null && sPlaybackManager.getState() != PlaybackStateCompat.STATE_NONE
                        && sPlaybackManager.getState() != PlaybackStateCompat.STATE_STOPPED
                        && !WindowUtils.getIsPause()) {
                    WindowUtils.showPopupWindow(AppApplication.this);
                    if (sPlaybackManager.getState() == PlaybackStateCompat.STATE_PAUSED ||
                            sPlaybackManager.getState() == PlaybackStateCompat.STATE_ERROR) {
                        Observable.timer(5, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        WindowUtils.setIsPause(true);
                                        WindowUtils.hidePopupWindow();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.e("---------------------------------------------onLowMemory---------------------------------------------------");
    }
}
