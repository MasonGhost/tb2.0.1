package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_PASSWORD_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_FIND_PASSWORD_V2;


/**
 * @Describe 密码相关
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public interface PasswordClient {

    public static final String REGITER_TYPE_EMAIL = "mail";
    public static final String REGITER_TYPE_SMS = "sms";

    @PUT(APP_PATH_CHANGE_PASSWORD_V2)
    Observable<Object> changePasswordV2(@Body Map<String,String> data);


    /**
     * 找回密码
     */
    @FormUrlEncoded
    @PUT(APP_PATH_FIND_PASSWORD_V2)
    Observable<CacheBean> findPasswordV2( @Field("phone") String phone
            , @Field("email") String email
            , @Field("verifiable_code") String vertifyCode
            , @Field("verifiable_type") String verifiable_type
            , @Field("password") String newPassword);
}
