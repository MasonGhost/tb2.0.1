package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GsonFollowFansBean;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/16
 * @contact email:450127106@qq.com
 */

public interface FollowFansClient {
    /**
     * 获取用户粉丝列表
     *
     * @param user_id
     * @param max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_FANS_LIST)
    Observable<BaseJson<GsonFollowFansBean>> getUserFansList(@Path("user_id") long user_id, @Path("max_id") long max_id, @Query("limit") Integer limitCount);

    /**
     * 获取用户关注列表
     *
     * @param user_id
     * @param max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_FOLLOW_LIST)
    Observable<BaseJson<GsonFollowFansBean>> getUserFollowsList(@Path("user_id") long user_id, @Path("max_id") long max_id, @Query("limit") Integer limitCount);

    /**
     * 关注用户操作
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_FOLLOW_USER)
    Observable<BaseJson> followUser(@Field("user_id") long user_id);

    /**
     * 取消用户关注
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @DELETE(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER)
    Observable<BaseJson> cancelFollowUser(@Field("user_id") long user_id);

}
