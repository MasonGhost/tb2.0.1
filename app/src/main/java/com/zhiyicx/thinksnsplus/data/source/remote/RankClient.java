package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Catherine
 * @describe 排行榜
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public interface RankClient {

    /**
     * 全站粉丝排行
     */
    @GET(ApiConfig.APP_PATH_RANK_ALL_FOLLOWER)
    Observable<List<UserInfoBean>> getRankFollower(@Query("limit") Long limit,
                                                   @Query("offset") int size);

    /**
     * 财富排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_ALL_RICHES)
    Observable<List<UserInfoBean>> getRankRiches(@Query("limit") Long limit,
                                                 @Query("offset") int size);

    /**
     * 收入排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_INCOME)
    Observable<List<UserInfoBean>> getRankIncome(@Query("limit") Long limit,
                                                 @Query("offset") int size);

    /**
     * 连续签到排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_CHECK_IN)
    Observable<List<UserInfoBean>> getRankCheckIn(@Query("limit") Long limit,
                                                  @Query("offset") int size);

    /**
     * 社区专家排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_QUESTION_EXPERTS)
    Observable<List<UserInfoBean>> getRankQuestionExpert(@Query("limit") Long limit,
                                                         @Query("offset") int size);

    /**
     * 问答达人排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_QUESTION_LIKES)
    Observable<List<UserInfoBean>> getRankQuestionLikes(@Query("limit") Long limit,
                                                        @Query("offset") int size);

    /**
     * 问答解答排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    @GET(ApiConfig.APP_PATH_RANK_QUESTION_ANSWER)
    Observable<List<UserInfoBean>> getRankAnswer(@Query("type") String type,
                                                 @Query("limit") Long limit,
                                                 @Query("offset") int size);

    /**
     * 动态排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    @GET(ApiConfig.APP_PATH_RANK_FEEDS)
    Observable<List<UserInfoBean>> getRankDynamic(@Query("type") String type,
                                                 @Query("limit") Long limit,
                                                 @Query("offset") int size);

    /**
     * 资讯排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    @GET(ApiConfig.APP_PATH_RANK_NEWS)
    Observable<List<UserInfoBean>> getRankInfo(@Query("type") String type,
                                                  @Query("limit") Long limit,
                                                  @Query("offset") int size);
}
