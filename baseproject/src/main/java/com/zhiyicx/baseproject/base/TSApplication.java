<<<<<<< HEAD
package com.zhiyicx.baseproject.base;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.crashhandler.CrashHandler;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
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


    @Override
    public void onCreate() {
        super.onCreate();
        // 处理app崩溃异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

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
    protected ImageModule getImageModule() {
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
=======
package com.zhiyicx.baseproject.base;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.crashhandler.CrashHandler;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
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


    @Override
    public void onCreate() {
        super.onCreate();
        // 处理app崩溃异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

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
    protected ImageModule getImageModule() {
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
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
