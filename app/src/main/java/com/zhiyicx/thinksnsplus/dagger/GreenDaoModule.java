package com.zhiyicx.thinksnsplus.dagger;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.source.local.UserCacheImpl;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class GreenDaoModule {
    private Context mContext;

    public GreenDaoModule(Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    UserCacheImpl provideUserCacheImpl() {
        return new UserCacheImpl(mContext);
    }
}
