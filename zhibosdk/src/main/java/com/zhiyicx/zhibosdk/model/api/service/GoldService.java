package com.zhiyicx.zhibosdk.model.api.service;

import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jess on 16/4/25.
 */
public interface GoldService {


    @FormUrlEncoded
    @POST(ZBApi.BASE_API)
    Observable<ZBBaseJson<String>> sendGift(@Field("api") String api,@Field("ak") String ak,
                                                 @Field("usid") String usid,
                                                 @Field("gift_code") String gift_code,   @Field("count") String count);

    @FormUrlEncoded
    @POST(ZBApi.BASE_API)
    Observable<ZBBaseJson<String>> sendZan(@Field("api") String api,@Field("ak") String ak,
                                           @Field("usid") String usid,
                                             @Field("count") String count);

}
