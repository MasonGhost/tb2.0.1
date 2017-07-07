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
    /**
     * @param phone 大陆地区合法手机号码，和email二选一
     * @param name 用户名，规则为 非数字和特殊字符开头，长度单字节字符算 0.5 长度，多字节字符算 1 长度，总长度不能超过 12
     * @param vertifyCode 验证码，长度在 4 - 6 位之间，目前暂定开发环境每 6 秒可获取一次，正式环境 300 秒。（以后增加后台可配）
     * @param password 用户密码，长度最小可无，最大不能超过 64 位
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_REGISTER)
    Observable<AuthBean> registerByPhone(@Field("phone") String phone
            , @Field("name") String name, @Field("verify_code") String vertifyCode, @Field("password") String password);
    /**
     * @param email 合法的邮箱地址，和phone二选一
     * @param name 用户名，规则为 非数字和特殊字符开头，长度单字节字符算 0.5 长度，多字节字符算 1 长度，总长度不能超过 12
     * @param vertifyCode 验证码，长度在 4 - 6 位之间，目前暂定开发环境每 6 秒可获取一次，正式环境 300 秒。（以后增加后台可配）
     * @param password 用户密码，长度最小可无，最大不能超过 64 位
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_REGISTER)
    Observable<AuthBean> registerByEmail(@Field("email") String email
            , @Field("name") String name, @Field("verify_code") String vertifyCode, @Field("password") String password);
}
