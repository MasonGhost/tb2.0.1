package com.zhiyicx.zhibosdk.model.api.service;


import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBApiPlay;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jess on 16/4/12.
 */
public interface VideoService {
    @FormUrlEncoded
    @POST
    Observable<ZBApiPlay> getVideoUrl(@Url String url,@Field("api") String api, @Field("ak") String ak,
                                      @Field("vid") String vid);


}
