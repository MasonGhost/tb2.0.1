package com.zhiyicx.baseproject.base;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.utils.imageloader.GlideImageLoaderStrategy;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.dagger.module.ImageModule;

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
        return new ImageModule(new GlideImageLoaderStrategy());
    }
}
