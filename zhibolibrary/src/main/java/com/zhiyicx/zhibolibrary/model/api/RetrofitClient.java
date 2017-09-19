package com.zhiyicx.zhibolibrary.model.api;


import com.zhiyicx.zhibolibrary.di.component.DaggerClientComponent;
import com.zhiyicx.zhibolibrary.di.module.ClientModule;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by radicsrichard on 15. 04. 28..
 */

public class RetrofitClient implements Baseclient {
    @Inject
    Retrofit mRetrofit;

    public RetrofitClient() {
        DaggerClientComponent
                .builder()
                .clientModule(new ClientModule())
                .build()
                .inject(this);
    }

    @Override
    public <T> T initRetrofit(Class<T> restInterface) {
        return mRetrofit.create(restInterface);//返回用于请求的服务
    }


}
