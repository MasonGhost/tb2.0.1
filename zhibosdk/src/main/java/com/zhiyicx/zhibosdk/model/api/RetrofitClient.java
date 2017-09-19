package com.zhiyicx.zhibosdk.model.api;

import javax.inject.Inject;

import retrofit2.Retrofit;


/**
 * Created by radicsrichard on 15. 04. 28..
 */

public class RetrofitClient implements Baseclient {
    @Inject
    Retrofit mRetrofit;

    public RetrofitClient() {

    }

    @Override
    public <T> T initRetrofit(Class<T> restInterface) {
        return mRetrofit.create(restInterface);//返回用于请求的服务
    }


}
