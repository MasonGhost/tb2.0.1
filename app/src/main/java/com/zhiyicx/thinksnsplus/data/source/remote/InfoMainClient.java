package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DYNAMIC_REPORT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_MY_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_COLLECTION_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DELETE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAIL;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAIL_RELATION;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DIG_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_FOLLOW_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_GET_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_LIST_TB;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_LIST_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REPORT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS_COUNT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS_USER_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TOP_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TYPE_V2;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description 资讯相关接口
 */
public interface InfoMainClient {

    // 获取资讯分类
    @GET(APP_PATH_INFO_TYPE_V2)
    Observable<InfoTypeBean> getInfoType();

    /**
     * 获取资讯列表
     *
     * @param key         搜索用的关键字
     * @param isRecommend 是否是推荐 1-推荐
     */
    @GET(APP_PATH_INFO_LIST_V2)
    Observable<List<InfoListDataBean>> getInfoListV2(@Query("cate_id") String cate_id,
                                                     @Query("after") Long max_id,
                                                     @Query("limit") Long limit,
                                                     @Query("page") Long page,
                                                     @Query("key") String key,
                                                     @Query("recommend") int isRecommend);

    /**
     * 获取置顶的资讯列表
     */
    @GET(APP_PATH_INFO_TOP_LIST)
    Observable<List<InfoListDataBean>> getInfoTopList(@Query("cate_id") String cate_id);

    /**
     * 获取资讯详情
     *
     * @param news_id 资讯id
     */
    @GET(APP_PATH_INFO_DETAIL)
    Observable<InfoListDataBean> getInfoDetail(@Path("news") String news_id);

    @GET(APP_PATH_INFO_DIG_LIST)
    Observable<List<InfoDigListBean>> getInfoDigList(@Path("news") String news_id,
                                                     @Query("after") Long max_id,
                                                     @Query("limit") int limit);

    /**
     * 获取一条资讯的相关资讯
     */
    @GET(APP_PATH_INFO_DETAIL_RELATION)
    Observable<List<InfoListDataBean>> getRelateInfoList(@Path("news") String news_id);

    @DELETE(APP_PATH_INFO_DELETE)
    Observable<BaseJsonV2<Object>> deleteInfo(@Path("category") String category, @Path("news") String news_id);

    // 获取收藏的资讯列表
    @GET(APP_PATH_INFO_COLLECTION_LIST)
    Observable<List<InfoListDataBean>> getInfoCollectListV2(@Query("after") Long after,
                                                            @Query("limit") Long limit);

    @GET(APP_PATH_GET_MY_INFO)
    Observable<List<InfoListDataBean>> getMyInfoList(@Query("after") Long max_id,
                                                     @Query("limit") Long limit, @Query("type") String type);

    // 订阅某类资讯
    @PATCH(APP_PATH_INFO_FOLLOW_LIST)
    Observable<BaseJsonV2<Object>> doSubscribe(@Body Map follows);


    @GET(APP_PATH_INFO_GET_COMMENT)
    Observable<InfoCommentBean> getInfoCommentListV2(@Path("news") String news_id,
                                                     @Query("after") Long max_id,
                                                     @Query("limit") Long limit);

    /**
     * 资讯投稿
     *
     * @param category
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(ApiConfig.APP_PATH_PUBLISH_INFO)
    Observable<BaseJsonV2<Object>> publishInfo(@Path("category") long category, @Body RequestBody body);

    /**
     * 置顶资讯
     *
     * @param news_id 资讯的唯一 id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_TOP_INFO)
    Observable<BaseJsonV2<Integer>> stickTopInfo(@Path("news_id") Long news_id, @Field("amount") long amount, @Field("day") int day);

    /**
     * 置顶资讯评论
     *
     * @param news_id
     * @param comment_id
     * @param amount
     * @param day
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_TOP_INFO_COMMENT)
    Observable<BaseJsonV2<Integer>> stickTopInfoComment(@Path("news_id") Long news_id, @Path("comment_id") Long comment_id, @Field("amount") long
            amount, @Field("day") int day);

    /*******************************************  打赏  *********************************************/


    /**
     * 对一条资讯打赏
     *
     * @param news_id 咨询 id
     * @param amount  打赏金额
     */
    @FormUrlEncoded
    @POST(APP_PATH_INFO_REWARDS)
    Observable<Object> rewardInfo(@Path("news_id") long news_id, @Field("amount") long amount);


    /**
     * 资讯打赏列表
     *
     * @param news_id    咨询 id
     * @param limit      列表返回数据条数
     * @param offset     翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     */
    @GET(APP_PATH_INFO_REWARDS_USER_LIST)
    Observable<List<RewardsListBean>> rewardInfoList(@Path("news_id") long news_id, @Query("limit") Integer limit, @Query("offset") Integer offset,
                                                     @Query("order") String order, @Query("order_type") String order_type);

    /**
     * 资讯打赏统计
     *
     * @param news_id 咨询 id
     */
    @GET(APP_PATH_INFO_REWARDS_COUNT)
    Observable<RewardsCountBean> getRewardCount(@Path("news_id") long news_id);

    /**
     * 获取资讯评论置顶审核列表 V2
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_REVIEW_INFO_COMMENT)
    Observable<List<TopNewsCommentListBean>> getNewsReviewComment(@Query("after") int after, @Query("limit")
            int limit);

    /**
     * 资讯评论置顶审核通过 V2
     *
     * @return
     */
    @PATCH(ApiConfig.APP_PATH_APPROVED_INFO_COMMENT)
    Observable<BaseJsonV2> approvedNewsTopComment(@Path("news_id") Long feed_id, @Path("comment_id")
            int comment_id, @Path("pinned_id") int pinned_id);

    /**
     * 更新编辑被驳回的投稿
     *
     * @return
     */
    @PATCH(ApiConfig.APP_PATH_UPDATE_INFO)
    Observable<BaseJsonV2<Object>> updateInfo(@Path("category_id") long cates_id, @Path("news_id") int news_id, @Body RequestBody requestBody);

    /**
     * 拒绝资讯评论置顶 V2
     *
     * @return
     */
    @PATCH(ApiConfig.APP_PATH_REFUSE_INFO_COMMENT)
    Observable<BaseJsonV2> refuseNewsTopComment(@Path("news_id") int news_id, @Path("comment_id") long comment_id, @Path("pinned_id") int pinned_id);

    /*******************************************  举报  *********************************************/

    /**
     * 举报一条资讯
     *
     * @param newsId 资讯 id
     * @param reason 举报原因
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_INFO_REPORT)
    Observable<ReportResultBean> reportInfo(@Path("news_id") String newsId, @Field("reason") String reason);


    /*******************************************  TB  *********************************************/


    /**
     * 获取资讯列表
     *
     * @param key  搜索用的关键字
     * @param type top-头条资讯 follow-关注机构资讯 默认top
     */
    @GET(APP_PATH_INFO_LIST_TB)
    Observable<List<InfoListDataBean>> getInfoListTB(@Query("cate_id") String cate_id,
                                                     @Query("after") Long max_id,
                                                     @Query("limit") Long limit,
                                                     @Query("page") Long page,
                                                     @Query("key") String key,
                                                     @Query("type") String type);
}