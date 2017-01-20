package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import rx.Observable;

/**
 * @author LiuChao
 * @describe 用户信息相关的网络请求
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public interface UserInfoClient {
    // 上传头像功能，写在CommonClient中

    /**
     * 修改用户资料
     *
     * @param userFieldMap 用户需要修改哪那些信息不确定
     */
    @FormUrlEncoded
    @PATCH("api/v1/users")
    Observable<BaseJson> changeUserInfo(@FieldMap HashMap<String, String> userFieldMap);
}
