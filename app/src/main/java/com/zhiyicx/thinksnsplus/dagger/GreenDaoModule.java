package com.zhiyicx.thinksnsplus.dagger;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@Module
public class GreenDaoModule {

    public GreenDaoModule() {
    }

    @Provides
    public UserInfoBeanGreenDaoImpl provideUserInfoBeanGreenDaoImpl(Application application) {
        return new UserInfoBeanGreenDaoImpl(application);
    }
}
