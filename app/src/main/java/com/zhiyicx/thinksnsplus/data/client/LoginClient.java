package com.zhiyicx.thinksnsplus.data.client;

import com.zhiyicx.common.base.BaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @GET("mockjs/2/test-get-repose-head-normal?")
    Observable<BaseJson<Integer>> login();
}
