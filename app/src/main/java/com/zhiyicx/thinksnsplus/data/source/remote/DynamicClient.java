package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author LiuChao
 * @describe 动态相关的接口
 * @date 2017/2/21
 * @contact email:450127106@qq.com
 */

public interface DynamicClient {
    /**
     * 发布动态
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(ApiConfig.APP_PATH_SEND_DYNAMIC)
    Observable<BaseJson<Object>> sendDynamic(@Body RequestBody body);
}
