package com.zhiyicx.zhibosdk.di.component;


import android.content.Context;

import com.zhiyicx.zhibosdk.di.module.ClientModule;
import com.zhiyicx.zhibosdk.di.module.ServiceModule;
import com.zhiyicx.zhibosdk.model.api.RetrofitClient;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@Singleton
@Component(modules = {ClientModule.class, ServiceModule.class})
public interface ClientComponent {
    void inject(RetrofitClient client);
    Context context(); // 提供Applicaiton的Context
    ZBServiceManager serviceManager();

    OkHttpClient okHttpClient();

}
