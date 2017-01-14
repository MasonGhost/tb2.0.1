package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public interface CommonClient {
    /**
     * 验证码类型
     * register: 注册
     * login: 登录
     * change: 修改,找回密码
     */

    public static final String VERTIFY_CODE_TYPE_REGISTER = "register";
    public static final String VERTIFY_CODE_TYPE_LOGIN = "login";
    public static final String VERTIFY_CODE_TYPE_CHANGE = "change";

    /**
     * @param requestState {requestState}=success/fasle
     * @param phone        需要被发送验证码的手机号
     * @param type         发送验证码的类型，固定三个值(register、login、change) register: 注
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/auth")
    Observable<BaseJson<CacheBean>> getVertifyCode(@Query("requestState") String requestState, @Field("phone") String phone
            , @Field("type") String type);
}
