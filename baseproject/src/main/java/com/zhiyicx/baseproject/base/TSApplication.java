package com.zhiyicx.baseproject.base;

import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.utils.SharePreferenceUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/21
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        // 处理app崩溃异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init();
        // 友盟
        UMShareAPI.get(this);
        MobclickAgent.setDebugMode(BuildConfig.USE_LOG);
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
