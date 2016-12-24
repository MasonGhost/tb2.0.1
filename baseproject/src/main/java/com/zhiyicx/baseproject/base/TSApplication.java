package com.zhiyicx.baseproject.base;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.UmengConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.utils.imageloader.GlideImageLoaderStrategy;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.dagger.module.ShareModule;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/21
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSApplication extends BaseApplication {


    /**
     * 网络根地址
     *
     * @return
     */
    @Override
    public String getBaseUrl() {
        return ApiConfig.APP_DOMAIN;
    }

    /**
     * 默认使用 glide,如果需要使用picasso等，请按照Gi{@Link GlideImageLoaderStrategy 配置}
     *
     * @return
     */
    @Override
    protected ImageModule getImagerModule() {
        PlatformConfig.setQQZone(UmengConfig.QQ_APPID, UmengConfig.QQ_SECRETKEY);
        PlatformConfig.setWeixin(UmengConfig.WEIXIN_APPID, UmengConfig.WEIXIN_SECRETKEY);
        PlatformConfig.setSinaWeibo(UmengConfig.SINA_APPID, UmengConfig.SINA_SECRETKEY);
        Config.REDIRECT_URL = UmengConfig.SINA_SECRETKEY;
        return new ImageModule(new GlideImageLoaderStrategy());
    }

    /**
     * 默认使用 umengshare,如果需要使用shareSDK等，请按照Gi{@Link UmengSharePolicyImpl 配置}
     *
     * @return
     */
    @Override
    protected ShareModule getShareModule() {
        return new ShareModule(new UmengSharePolicyImpl(getApplicationContext()));
    }

}
