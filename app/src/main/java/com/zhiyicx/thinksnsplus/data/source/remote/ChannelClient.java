package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface ChannelClient {
    /**
     * 订阅某个频道
     *
     * @param channel_id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_HANDLE_SUBSCRIB_CHANNEL)
    Observable<BaseJson<Object>> subscribChannel(@Path("channel_id") long channel_id);

    /**
     * 取消某个频道的订阅
     *
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_HANDLE_SUBSCRIB_CHANNEL)
    Observable<BaseJson<Object>> cancleSubscribChannel(@Path("channel_id") long channel_id);

    /**
     * 获取频道列表
     *
     * @param type 频道类型 “”表示所有的频道  “my”表示我关注的频道
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_CHANNEL)
    Observable<BaseJson<List<ChannelInfoBean>>> getChannelList(@Path("type") String type);

    /**
     * 获取频道的动态列表
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_CHANNEL_DYNAMIC_LIST)
    Observable<BaseJson<List<DynamicBean>>> getDynamicListFromChannel(@Path("channel_id") long channel_id,
                                                                      @Query("limit") int limit, @Query("max_id") int max_id);
}
