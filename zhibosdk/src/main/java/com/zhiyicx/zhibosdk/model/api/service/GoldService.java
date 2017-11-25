package com.zhiyicx.zhibosdk.model.api.service;

import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jess on 16/4/25.
 */
public interface GoldService {


    @FormUrlEncoded
    @POST
    Observable<ZBBaseJson<String>> sendGift(@Url String url,@Field("api") String api, @Field("ak") String ak,
                                            @Field("usid") String usid,
                                            @Field("gift_code") String gift_code, @Field("count") String count);

    @FormUrlEncoded
    @POST
    Observable<ZBBaseJson<String>> sendZan(@Url String url,@Field("api") String api,@Field("ak") String ak,
                                           @Field("usid") String usid,
                                             @Field("count") String count);

}
