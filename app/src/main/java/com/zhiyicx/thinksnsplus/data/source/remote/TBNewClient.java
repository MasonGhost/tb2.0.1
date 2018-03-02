package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Describe TB 项目新的接口
 * @Author Jungle68
 * @Date 2018/3/2
 * @Contact master.jungle68@gmail.com
 */
public interface TBNewClient {

    /**
     * 全站粉丝排行
     */
    @GET(ApiConfig.APP_PATH_RANK_ALL_FOLLOWER)
    Observable<List<UserInfoBean>> getRankFollower(@Query("limit") Long limit,
                                                   @Query("offset") int size);


}
