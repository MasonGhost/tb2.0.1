package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
     * @param fieldMap
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_SEND_DYNAMIC)
    Observable<BaseJson<Object>> sendDynamic(@FieldMap HashMap<String, Object> fieldMap);
}
