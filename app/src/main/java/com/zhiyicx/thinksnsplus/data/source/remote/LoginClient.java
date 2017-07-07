package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LOGIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LOGIN_V2;

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

    /**
     *
     * @param account  phone 用户用于登陆的中国大陆合法手机号码，和account二选一
     * @param account  	用户账号，可以是手机号、邮箱、用户名、用户ID，和phone二选一
     * @param password  用户登陆密码
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_LOGIN_V2)
    Observable<AuthBean> loginV2(@Field("account") String account
            , @Field("password") String password);

}
