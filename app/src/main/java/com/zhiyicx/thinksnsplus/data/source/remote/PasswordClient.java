package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe 登录相关的网络请求
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public interface PasswordClient {
    /**
     * 修改密码
     * @param requestState
     * @param password
     * @param newPassword
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/auth")
    Observable<BaseJson<CacheBean>> changePassword(@Query("requestState") String requestState, @Field("password") String password
            , @Field("new_password") String newPassword);

    /**
     *  找回密码
     * @param requestState
     * @param phone
     * @param vertifyCode
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/auth")
    Observable<BaseJson<CacheBean>> findPassword(@Query("requestState") String requestState, @Field("phone") String phone
          , @Field("code") String vertifyCode, @Field("password") String newPassword);
}
