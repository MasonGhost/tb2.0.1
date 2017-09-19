package com.zhiyicx.zhibosdk.model.api.service;


import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBApiPlay;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jess on 16/4/12.
 */
public interface VideoService {
    @FormUrlEncoded
    @POST(ZBApi.BASE_API)
    Observable<ZBApiPlay> getVideoUrl(@Field("api") String api, @Field("ak") String ak,
                                      @Field("vid") String vid);


}
