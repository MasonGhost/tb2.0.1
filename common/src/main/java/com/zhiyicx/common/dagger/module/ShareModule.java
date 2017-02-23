package com.zhiyicx.common.dagger.module;


import com.zhiyicx.common.thridmanager.share.SharePolicy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

@Module
public class ShareModule {
    SharePolicy mSharePolicy;

    public ShareModule(SharePolicy sharePolicy) {
        this.mSharePolicy = sharePolicy;
    }

    @Singleton
    @Provides
    public SharePolicy provideSharePolicy() {
        return mSharePolicy;
    }
}
