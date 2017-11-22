package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.module.ClientModule;
import com.zhiyicx.zhibolibrary.di.module.ServiceModule;
import com.zhiyicx.zhibolibrary.model.api.RetrofitClient;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;

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

    ServiceManager serviceManager();

    OkHttpClient okHttpClient();

}
