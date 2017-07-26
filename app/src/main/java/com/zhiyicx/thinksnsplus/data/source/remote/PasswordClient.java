package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_PASSWORD;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_FIND_PASSWORD;
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

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_CHANGE_PASSWORD)
    Observable<BaseJson<CacheBean>> changePassword(@Query("requestState") String requestState, @Field("password") String password
            , @Field("new_password") String newPassword);

    /**
     * 找回密码
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_FIND_PASSWORD)
    Observable<BaseJson<CacheBean>> findPassword(
            @Query("requestState") String requestState
            , @Field("phone") String phone
            , @Field("email") String email
            , @Field("code") String vertifyCode
            , @Field("password") String newPassword);


    @FormUrlEncoded
    @PUT(APP_PATH_FIND_PASSWORD_V2)
    Observable<CacheBean> findPasswordV2( @Field("phone") String phone
            , @Field("email") String email
            , @Field("verifiable_code") String vertifyCode
            , @Field("verifiable_type") String verifiable_type
            , @Field("password") String newPassword);
}
