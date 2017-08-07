package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_COLLECT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_COLLECT_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_COMMENT_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_FOLLOW_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS_COUNT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS_USER_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_SEARCH;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TYPE_V2;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoMainClient {

    // 获取资讯分类
    @GET(APP_PATH_INFO_TYPE_V2)
    Observable<InfoTypeBean> getInfoType();
//    Observable<BaseJson<InfoTypeBean>> getInfoType();

    // 获取资讯分类
    @GET(APP_PATH_INFO_DETAILS)
    Observable<BaseJson<InfoWebBean>> getInfoWebContent(@Path("news_id") String news_id);

    // 获取某类资讯列表
    @GET(APP_PATH_INFO_LIST)
    Observable<BaseJson<InfoListBean>> getInfoList(@Query("cate_id") String cate_id,
                                                   @Query("max_id") Long max_id,
                                                   @Query("limit") Long limit,
                                                   @Query("page") Long page);

    // 获取收藏的资讯列表
    @GET(APP_PATH_INFO_COLLECT_LIST)
    Observable<BaseJson<List<InfoListDataBean>>> getInfoCollectList(@Query("max_id") Long max_id,
                                                                    @Query("limit") Long limit,
                                                                    @Query("page") Long page);

    // 订阅某类资讯
    @FormUrlEncoded
    @POST(APP_PATH_INFO_FOLLOW_LIST)
    Observable<BaseJson<Integer>> doSubscribe(@Field("follows") String follows);

    // 收藏资讯
    @POST(APP_PATH_INFO_COLLECT)
    Observable<BaseJson<Integer>> collectInfo(@Path("news_id") String news_id);

    // 取消收藏资讯
    @DELETE(APP_PATH_INFO_COLLECT)
    Observable<BaseJson<Integer>> cancleCollectInfo(@Path("news_id") String news_id);

    // 获取一条资讯的评论列表
    @GET(APP_PATH_INFO_COMMENT_LIST)
    Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(@Path("feed_id") String feed_id,
                                                                       @Query("max_id") Long max_id,
                                                                       @Query("limit") Long limit);

    @GET(APP_PATH_INFO_SEARCH)
    Observable<BaseJson<List<InfoListDataBean>>> searchInfoList(@Query("key") String key,
                                                                @Query("max_id") Long max_id,
                                                                @Query("limit") Long limit);

    /**
     * 对一条资讯或一条资讯评论进行评论
     *
     * @param comment_content  内容
     * @param reply_to_user_id 被评论者id 对评论进行评论时传入
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_INFO_COMMENT)
    Observable<BaseJson<Integer>> commentInfo(@Field("comment_content") String comment_content,
                                              @Field("reply_to_user_id") int reply_to_user_id);

    /*******************************************  打赏  *********************************************/


    /**
     * 对一条资讯打赏
     *
     * @param news_id 咨询 id
     * @param amount  打赏金额
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_INFO_REWARDS)
    Observable<Object> rewardInfo(@Path("news_id") long news_id, @Field("amount") float amount);


    /**
     * 资讯打赏列表
     *
     * @param news_id    咨询 id
     * @param limit      列表返回数据条数
     * @param since      翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     * @return
     */
    @GET(APP_PATH_INFO_REWARDS_USER_LIST)
    Observable<List<RewardsListBean>> rewardInfoList(@Path("news_id") long news_id, @Query("limit") Integer limit, @Query("since") Integer since, @Query("order") String order, @Query("order_type") String order_type);

    /**
     * 资讯打赏统计
     *
     * @param news_id 咨询 id
     * @return
     */
    @GET(APP_PATH_INFO_REWARDS_COUNT)
    Observable<RewardsCountBean> getRewardCount(@Path("news_id") long news_id);
}