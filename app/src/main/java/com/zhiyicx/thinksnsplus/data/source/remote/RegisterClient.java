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
 * @describe 登陆相关的网络请求
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public interface RegisterClient {
    /**
     *
     * @param requestState
     * @param phone
     * @param name
     * @param vertifyCode
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/auth")
    Observable<BaseJson<CacheBean>> register(@Query("requestState") String requestState, @Field("phone") String phone
            , @Field("name") String name, @Field("vertifyCode") String vertifyCode, @Field("password") String password);
}
