package com.zhiyicx.thinksnsplus.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.danikula.videocache.HttpProxyCacheServer;
import com.github.tamir7.contacts.Contacts;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pingplusplus.android.Pingpp;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.zhiyicx.appupdate.AppUpdateManager;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.base.TSApplication;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.net.HttpsSSLFactroyUtils;
import com.zhiyicx.common.net.intercept.CommonRequestIntercept;
import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.appprocess.AndroidProcess;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.manage.ZBIMSDK;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.QueueManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.simple.eventbus.EventBus;

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

import static com.zhiyicx.thinksnsplus.config.ErrorCodeConfig.AUTH_FAIL;

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

    /**
     * 当前登录用户的信息
     */
    private static AuthBean mCurrentLoginAuth;
    private static HttpProxyCacheServer mMediaProxyCacheServer;
    private static QueueManager sQueueManager;
    private static PlaybackManager sPlaybackManager;
    private static String TOKEN = "none";
    public static List<Integer> sOverRead = new ArrayList<>();

    public int mActivityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = AndroidProcess.getProcessName(this, android.os.Process.myPid());
        if (processName != null && processName.equals(getPackageName())) {
            initAppProject();
        }
        // 极光推送
        JPushInterface.setDebugMode(BuildConfig.USE_LOG);
        JPushInterface.init(this);
    }

    /**
     * 应用进程只需要启动一次
     */
    private void initAppProject() {
        initComponent();
        // 安装了 IM
        if (!TextUtils.isEmpty(mSystemRepository.getBootstrappersInfoFromLocal().getIm_serve())) {
            LogUtils.d(TAG, "---------------start IM---------------------");
            ZBIMSDK.init(getContext());
        }
        // 开启后台任务
        BackgroundTaskManager.getInstance(getContext()).startBackgroundTask();
        registerActivityCallBacks();
        // ping++
        Pingpp.enableDebugLog(BuildConfig.USE_LOG);
        // 通讯录
        Contacts.initialize(this);

    }

    /**
     * 增加统一请求头
     *
     * @return
     */
    @Override
    protected Set<Interceptor> getInterceptors() {
        //统一请求头数据
        Map<String, String> params = new HashMap<>();
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
                    originalResponse) {
                // 处理 head请求
                handleHeadRequest(originalResponse);
                // 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                // token过期，调到登陆页面重新请求token,
                BaseJson baseJson = null;
                try {
                    baseJson = new Gson().fromJson(httpResult, BaseJson.class);
                } catch (JsonSyntaxException e) {
//                    LogUtils.e("Invalid Json length:::"+httpResult.length());
                }
                if (originalResponse.code() == AUTH_FAIL) {
                    if (mAuthRepository.isNeededRefreshToken()) {
                        handleAuthFail(getString(R.string.auth_fail_relogin));
                    } else {
                        handleAuthFail(getString(R.string.code_1015));
                    }
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
                return originalResponse;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的 requeat 如增加 header,不做操作则返回 request
                AuthBean authBean = mAuthRepository.getAuthBean();
                if (authBean != null) {
                    return chain.request().newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", " Bearer " + authBean.getToken())
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
     * 未读数处理
     *
     * @param originalResponse
     */
    private void handleHeadRequest(Response originalResponse) {

        if (originalResponse != null && originalResponse.header("unread-notification-limit") != null) {
            EventBus.getDefault().post(originalResponse.header("unread-notification-limit"), EventBusTagConfig.EVENT_UNREAD_NOTIFICATION_LIMIT);
        }

    }

    /**
     * 认证失败弹框
     *
     * @param tipStr
     */
    private void handleAuthFail(final String tipStr) {
        if (!(ActivityHandler
                .getInstance().currentActivity() instanceof LoginActivity) && ActivityHandler
                .getInstance().currentActivity() instanceof TSActivity) {
            ((TSActivity) ActivityHandler
                    .getInstance().currentActivity()).showWarnningDialog(tipStr, (dialog, which) -> {
                // TODO: 2017/2/8  清理登录信息 token 信息
                mAuthRepository.clearAuthBean();
                mAuthRepository.clearThridAuth();

                Intent intent = new Intent
                        (getContext(),
                                LoginActivity
                                        .class);
                ActivityHandler.getInstance()
                        .currentActivity()
                        .startActivity
                                (intent);
                dialog.dismiss();
            });
        }

    }

    /**
     * rx 对 mErrorHandler 错误统一回调处理
     *
     * @return
     */
    @Override
    protected ResponseErroListener getResponseErroListener() {
        return (context, e) -> LogUtils.d(TAG, "------------>" + e.getMessage());
    }

    /**
     * 初始化Component
     */
    public void initComponent() {
        AppComponent appComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())
                .httpClientModule(getHttpClientModule())
                .imageModule(getImageModule())
                .serviceModule(getServiceModule())
                .cacheModule(getCacheModule())
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
        if (ApiConfig.APP_IS_NEED_SSH_CERTIFICATE) {
            return super.getSSLSocketFactory();
        } else {
            int[] a = {R.raw.plus};
            return HttpsSSLFactroyUtils.getSSLSocketFactory(this, a);
        }
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

    public static long getMyUserIdWithdefault() {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        long userId;
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
        if (mCurrentLoginAuth != null) {
            TOKEN = mCurrentLoginAuth.getToken();
        }
    }

    public static String getTOKEN() {
        return "Bearer " + (AppApplication.mCurrentLoginAuth == null ? "" : AppApplication.mCurrentLoginAuth.getToken());
    }

    public static HttpProxyCacheServer getProxy() {
        return AppApplication.mMediaProxyCacheServer == null ? (AppApplication
                .mMediaProxyCacheServer = newProxy()) : AppApplication.mMediaProxyCacheServer;
    }

    private static HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(BaseApplication.getContext())
                // liuchao 2017.3.27修改获取缓存路径
                .cacheDirectory(new File(FileUtils.getCacheFile(BaseApplication.getContext(), false)
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
                // 切到后台
                if (mActivityCount == 0) {
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
                if (activity instanceof MusicPlayActivity || activity instanceof GalleryActivity || activity instanceof SelectDynamicTypeActivity) {
                    WindowUtils.hidePopupWindow();
                } else if (sPlaybackManager != null && sPlaybackManager.getState() != PlaybackStateCompat.STATE_NONE
                        && sPlaybackManager.getState() != PlaybackStateCompat.STATE_STOPPED
                        && !WindowUtils.getIsPause()) {
                    WindowUtils.showPopupWindow(AppApplication.this);
                    if (sPlaybackManager.getState() == PlaybackStateCompat.STATE_PAUSED ||
                            sPlaybackManager.getState() == PlaybackStateCompat.STATE_ERROR) {
                        Observable.timer(5, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    WindowUtils.setIsPause(true);
                                    WindowUtils.hidePopupWindow();
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
