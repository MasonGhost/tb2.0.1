package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LOGIN;

/**
 * @author LiuChao
 * @describe 登录相关的网络请求
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public interface LoginClient {
    /**
     * 用户输入手机号和密码进行登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_LOGIN)
    Observable<BaseJson<AuthBean>> login(@Query("requestState") String requestState, @Field("phone") String phone
            , @Field("password") String password, @Field("device_code") String device_code);
}
