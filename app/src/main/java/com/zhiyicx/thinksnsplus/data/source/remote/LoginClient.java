package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.LoginBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe 登陆相关的网络请求
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public interface LoginClient {
    /**
     * 用户输入手机号和密码进行登陆
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/auth")
    Observable<BaseJson<LoginBean>> login(@Query("requestState") String requestState, @Field("phone") String phone
            , @Field("password") String password,@Field("device_code") String device_code);
}
