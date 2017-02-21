package com.zhiyicx.thinksnsplus.base;

import android.app.Application;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.dagger.module.ShareModule;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.rxerrorhandler.RxErrorHandler;
import com.zhiyicx.thinksnsplus.dagger.GreenDaoModule;
import com.zhiyicx.thinksnsplus.data.source.local.CacheManager;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SendDynamicPresenterRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository_Factory;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

@Singleton
@Component(modules = {AppModule.class, HttpClientModule.class, ServiceModule.class, CacheModule.class, ImageModule.class, ShareModule.class, GreenDaoModule.class})
public interface AppComponent extends InjectComponent<AppApplication> {
    void inject(BackgroundTaskHandler backgroundTaskHandler);

    Application Application();

    //服务管理器,retrofitApi
    ServiceManager serviceManager();

    //缓存管理器
    CacheManager cacheManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();

    //用于请求权限,适配6.0的权限管理
    RxPermissions rxPermissions();

    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();

    SharePolicy sharePolicy();

    AuthRepository authRepository();

    UserInfoBeanGreenDaoImpl userInfoBeanGreenDao();

    FollowFansBeanGreenDaoImpl followFansBeanGreenDao();

    UserInfoRepository userInfoRepository();

    SendDynamicPresenterRepository dynamicPresenterRepository();

    UpLoadRepository uploadRepository();
}
