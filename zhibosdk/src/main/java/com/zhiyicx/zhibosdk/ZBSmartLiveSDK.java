package com.zhiyicx.zhibosdk;

import android.app.Application;

import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.zhiyicx.imsdk.manage.ZBIMSDK;
import com.zhiyicx.zhibosdk.di.component.ClientComponent;
import com.zhiyicx.zhibosdk.di.component.DaggerClientComponent;
import com.zhiyicx.zhibosdk.di.module.ClientModule;
import com.zhiyicx.zhibosdk.di.module.ServiceModule;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.model.api.ZBApi;

/**
 * Created by jungle on 16/7/5.
 * com.zhiyicx.zhibo.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBSmartLiveSDK {
    private static final String TAG = "ZBSmartLiveSDK";

    private static ClientComponent mClientComponent;

    public ZBSmartLiveSDK() {
    }

    /**
     * 初始化设置根域名和域名版本号
     * @param application
     * @param rootDomain
     * @param apiVersion
     */
    public static void init(Application application,String rootDomain, String apiVersion) {
        ZBApi.ZHIBO_DOMAIN = rootDomain+ZBApi.BASE_API;
        ZBApi.ZHIBO_DOMAIN_VERSION = apiVersion;
        setClientComponent(application);
        /**
         * 推流初始化
         */
        StreamingEnv.init(application);
        ZBIMSDK.init(application);

        new ZBInitConfigManager()
                .initConfig(application);
    }


    private static void setClientComponent(Application application) {
        if (mClientComponent == null) {
            mClientComponent = DaggerClientComponent
                    .builder()
                    .clientModule(new ClientModule(application))
                    .serviceModule(new ServiceModule())
                    .build();
        }
    }

    public static ClientComponent getClientComponent() {
        if (mClientComponent == null)
            throw new RuntimeException("还未调用初始化,ZBSmartLiveSDK.init(ApplicationContext)");
        return mClientComponent;
    }


}

