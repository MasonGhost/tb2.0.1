package com.zhiyicx.thinksnsplus.data.source.remote;

import retrofit2.http.GET;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LIVE_GET_TICKET;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */
public interface LiveClient {


    @GET(APP_PATH_LIVE_GET_TICKET)
    Observable<String> getLiveTicket();

}
