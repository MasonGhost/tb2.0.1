package com.zhiyicx.baseproject.base;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.crashhandler.CrashClient;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/21
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSApplication extends BaseApplication {
    private static final int DEFAULT_TOAST_SHORT_DISPLAY_TIME = 2_000;


    @Override
    public void onCreate() {
        super.onCreate();
        /// 处理app崩溃异常
        CrashClient.init(new CrashClient.CrashListener() {
            @SuppressLint("MyToastHelper")
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                Toast.makeText(BaseApplication.getContext(), R.string.app_crash_tip, Toast.LENGTH_SHORT).show();
                rx.Observable.timer(DEFAULT_TOAST_SHORT_DISPLAY_TIME, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                ActivityHandler.getInstance().AppExit();
                            }
                        });
            }
        });
        /*
         * 友盟
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(getApplicationContext()).setShareConfig(config);
        UMShareAPI.get(this);
        MobclickAgent.setDebugMode(BuildConfig.USE_LOG);
        // 腾讯 x5 内核
        initWebX5();
    }

    private void initWebX5() {
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                LogUtils.i("web x5 : onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtils.i("web x5 : onViewInitFinished " + b);

            }
        });
    }

    /**
     * 网络根地址
     *
     * @return
     */
    @Override
    public String getBaseUrl() {
        if (BuildConfig.USE_DOMAIN_SWITCH) {
            String domain = SharePreferenceUtils.getString(getContext(), SharePreferenceUtils.SP_DOMAIN);
            if (!TextUtils.isEmpty(domain)) {
                ApiConfig.APP_DOMAIN = domain;
            }
        }
        return ApiConfig.APP_DOMAIN;
    }

    /**
     * 默认使用 glide,如果需要使用picasso等，请按照Gi{@Link GlideImageLoaderStrategy 配置}
     *
     * @return
     */
    @Override
    protected ImageModule getImageModule() {
        return new ImageModule(new GlideImageLoaderStrategy());
    }


}
