package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REGISTER;

/**
 * @author LiuChao
 * @describe 登录相关的网络请求
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public interface RegisterClient {
    public static final String REGITER_TYPE_EMAIL = "mail";
    public static final String REGITER_TYPE_SMS = "sms";

    /**
     * @param phone           Required without email, User China phone bumber.
     * @param email           Required withput phone, User E-Mail.
     * @param name            Required, User registerByPhone name.
     * @param password        用户密码，长度最小可无，最大不能超过 64 位4
     * @param verifiable_type Required, Notification serve verification type.
     * @param verifiable_code Required, Verification code.
     * @param type type, 只有两个值 personal 和 group
     *          personal 个人用户注册类型值
     *          group 组织用户注册类型值
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_REGISTER)
    Observable<AuthBean> register(@Field("phone") String phone
            , @Field("email") String email
            , @Field("name") String name
            , @Field("password") String password
            , @Field("verifiable_type") String verifiable_type
            , @Field("verifiable_code") String verifiable_code
            , @Field("type") String type);

}
