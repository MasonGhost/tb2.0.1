package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_PASSWORD;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_FIND_PASSWORD;


/**
 * @Describe 密码相关
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
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
    @PATCH(APP_PATH_CHANGE_PASSWORD)
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
    @PATCH(APP_PATH_FIND_PASSWORD)
    Observable<BaseJson<CacheBean>> findPassword(@Query("requestState") String requestState, @Field("phone") String phone
          , @Field("code") String vertifyCode, @Field("password") String newPassword);
}
