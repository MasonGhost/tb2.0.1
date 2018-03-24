package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.thinksnsplus.data.beans.AuthBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LOGIN;

/**
 * @author LiuChao
 * @describe 登录相关的网络请求
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public interface LoginClient {

    /**
     *
     * @param login  Required,HintSideBarUserBean auth field. Can use name / email / phone
     * @param password  用户登陆密码
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_LOGIN)
    Observable<AuthBean> loginV2(@Field("login") String login
            , @Field("password") String password);

}
