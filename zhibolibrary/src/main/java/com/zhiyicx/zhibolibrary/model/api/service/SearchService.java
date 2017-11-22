package com.zhiyicx.zhibolibrary.model.api.service;


import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.SearchJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhiyicx on 2016/4/5.
 */
public interface SearchService {
    /**
     * 搜索用户
     * @param accessKey 登录时返回的授权信息
     * @param secretKey 登录时返回的授权信息
     * @param keyword   搜索关键字
     * @param page      分页页数 default:1,分页变量请在获取配置API中获取
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<SearchJson> Search(@Field("api") String api, @Field("auth_accesskey") String accessKey,
                                  @Field("auth_secretkey") String secretKey,
                                  @Field("keyword") String keyword,
                                  @Field("p") int page
    );



}
