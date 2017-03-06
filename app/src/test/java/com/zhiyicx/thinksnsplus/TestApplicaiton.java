package com.zhiyicx.thinksnsplus;

import android.support.annotation.NonNull;

import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.CacheModule;
import com.zhiyicx.thinksnsplus.base.ServiceModule;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/29
 * @Contact master.jungle68@gmail.com
 */

public class TestApplicaiton extends AppApplication {
    AppModule mAppModule;
    HttpClientModule mHttpClientModule;
    ImageModule mImageModule;
    ShareModule mShareModule;
    ServiceModule mServiceModule;
    CacheModule mCacheModule;

    @Override
    public AppModule getAppModule() {
        if(mAppModule==null){
            return super.getAppModule();
        }
        return mAppModule;
    }

    @Override
    public HttpClientModule getHttpClientModule() {
        if (mHttpClientModule == null) {
            return super.getHttpClientModule();
        }
        return mHttpClientModule;
    }

    @Override
    public ImageModule getImageModule() {
        if (mImageModule == null) {
            return super.getImageModule();
        }
        return mImageModule;
    }

    @NonNull
    @Override
    public ServiceModule getServiceModule() {
        if (mShareModule == null) {
            return super.getServiceModule();
        }
        return mServiceModule;
    }

    @NonNull
    @Override
    public CacheModule getCacheModule() {
        if (mCacheModule == null) {
            return super.getCacheModule();
        }
        return mCacheModule;
    }

    public void setAppModule(AppModule appModule) {
        mAppModule = appModule;
    }

    public void setHttpClientModule(HttpClientModule httpClientModule) {
        mHttpClientModule = httpClientModule;
    }

    public void setImageModule(ImageModule imageModule) {
        mImageModule = imageModule;
    }

    public void setShareModule(ShareModule shareModule) {
        mShareModule = shareModule;
    }

    public void setServiceModule(ServiceModule serviceModule) {
        mServiceModule = serviceModule;
    }

    public void setCacheModule(CacheModule cacheModule) {
        mCacheModule = cacheModule;
    }
}
