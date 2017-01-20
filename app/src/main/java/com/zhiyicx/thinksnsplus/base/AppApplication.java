package com.zhiyicx.thinksnsplus.base;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.net.HttpsSSLFactroyUtils;
import com.zhiyicx.common.net.intercept.CommonRequestIntercept;
import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.manage.ZBIMSDK;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class AppApplication extends TSApplication {

    AuthRepository mAuthRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        ZBIMSDK.init(getContext());
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
            public Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response response) {
                // 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                // token过期，调到登陆页面重新请求token,
                BaseJson baseJson = new Gson().fromJson(httpResult, BaseJson.class);
                if (baseJson.getCode() == ErrorCodeConfig.TOKEN_EXPIERD
                        || baseJson.getCode() == ErrorCodeConfig.NEED_RELOGIN
                        || baseJson.getCode() == ErrorCodeConfig.OTHER_DEVICE_LOGIN
                        || baseJson.getCode() == ErrorCodeConfig.TOKEN_NOT_EXIST) {
                    // 跳到登陆页面，销毁之前的所有页面
                    ActivityHandler.getInstance().finishAllActivity();
                    startActivity(new Intent(AppApplication.this, LoginActivity.class));
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
                .shareModule(getShareModule())// 分享框架
                .serviceModule(getServiceModule())// 需自行创建
                .cacheModule(getCacheModule())// 需自行创建
                .build();
        AppComponentHolder.setAppComponent(appComponent);
        mAuthRepository = appComponent.authRepository();
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
        AuthBean authBean = SharePreferenceUtils.getObject(this, AuthBean.SHAREPREFERENCE_TAG);
        if (!isNeededRefreshToken(authBean)) {
            return;
        }
        CommonClient commonClient = AppComponentHolder.getAppComponent().serviceManager().getCommonClient();
        String imei = DeviceUtils.getIMEI(this);
        commonClient.refreshToken(authBean.getRefresh_token(), imei)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(2, 2))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<AuthBean>>() {
                    @Override
                    public void call(BaseJson<AuthBean> authBeanBaseJson) {
                        if (authBeanBaseJson.isStatus() || authBeanBaseJson.getCode() == 0) {
                            // 获取了最新的token，将这些信息保存起来
                            SharePreferenceUtils.saveObject(AppApplication.this, AuthBean.SHAREPREFERENCE_TAG, authBeanBaseJson.getData());
                        }
                    }
                });
    }

    /**
     * 是否需要刷新token
     *
     * @return
     */
    private boolean isNeededRefreshToken(AuthBean authBean) {
        long createTime = authBean.getCreated_at();
        int expiers = authBean.getExpires();
        int days = TimeUtils.getifferenceDays((createTime + expiers) * 1000);//表示token过期时间距离现在的时间
        if (expiers == 0) {// 永不过期,不需要刷新token
            return false;
        } else if (days >= -1) {// 表示当前时间是过期时间的前一天,或者已经过期,需要尝试刷新token
            return true;
        }
        return false;
    }
}
