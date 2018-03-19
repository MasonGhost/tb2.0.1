package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleMemberCountBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopCircleJoinReQuestBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostListBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleCommentZip;
import com.zhiyicx.thinksnsplus.data.beans.circle.CirclePostBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_APPROVED_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_APPROVED_POST_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_APPROVE_CIRCLE_REPOT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_ATTORN_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CANCEL_CIRCLE_MEMBERS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CIRCLE_POST_REPOT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CIRCLE_REPOT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_COLLECTLIST_POST_FORMAT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_COMMENT_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_COMMENT_REPOT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CREATE_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DEAL_CIRCLE_BLACKLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DEAL_CIRCLE_MANAGER;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DEAL_CIRCLE_MEMBER_JOIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ALL_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ALL_POSTLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLEDETAIL;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLELIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLEMEMBERS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_CATEGROIES;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_COUNT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_EARNINGLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_MEMBER_JOIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_REPOTS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CREATE_RULE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_GROUP_MEMBER_COUNT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_MINE_POSTLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_MY_JOINED_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_POSTLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_RECOMMEND_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_REWARD_POST_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ROUNDCIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_TOP_POST_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_USER_COLLECT_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LIKE_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MANAGER_TOP_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_PUBLISH_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_PUT_EXIT_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_PUT_JOIN_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REFUSE_CIRCLE_REPOT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REFUSE_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REFUSE_POST_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REWARD_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SET_CIRCLE_PERMISSIONS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_TOP_POST_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_UNDO_TOP_POST;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:39
 * @Email Jliuer@aliyun.com
 * @Description 新的圈子相关接口
 */
public interface CircleClient {

    /**
     * 我加入的圈子接口
     * 默认: join, join 我加入 audit 待审核, allow_post 可以发帖的, random 随机
     */
    enum MineCircleType {
        JOIN("join"),
        AUDIT("audit"),
        SEARCH("search"),
        ALLOW("allow_post"),
        RANDOM("random");
        public String value;

        MineCircleType(String value) {
            this.value = value;
        }
    }

    /**
     * @param limit  返回条数 默认为15
     * @param offset 翻页偏移量
     * @author Jliuer
     * @Date 17/11/27 17:07
     * @Email Jliuer@aliyun.com
     * @Description 获取圈子分类
     */
    @GET(APP_PATH_GET_CIRCLE_CATEGROIES)
    Observable<List<CircleTypeBean>> getCategroiesList(@Query("limit") int limit, @Query("offset") int offset);

    /**
     * 获取圈子列表
     *
     * @param categoryId 圈子类别id
     * @param limit
     * @param offset
     * @return
     */
    @GET(APP_PATH_GET_CIRCLELIST)
    Observable<List<CircleInfo>> getCircleList(@Path("category_id") long categoryId, @Query("limit") int limit, @Query("offset") int offset);

    /**
     * 获取推荐的圈子
     *
     * @param limit  默认 20 ，数据返回条数 默认为20
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type   random 随机
     * @return
     */
    @GET(APP_PATH_GET_RECOMMEND_CIRCLE)
    Observable<List<CircleInfo>> getRecommendCircle(@Query("limit") int limit, @Query("offset") int offset, @Query("type") String type);

    /**
     * 获取已经加入的圈子
     *
     * @param limit  默认 20 ，数据返回条数 默认为20
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type   默认: join, join 我加入 audit 待审核
     * @return
     */
    @GET(APP_PATH_GET_MY_JOINED_CIRCLE)
    Observable<List<CircleInfo>> getMyJoinedCircle(@Query("limit") Integer limit, @Query("offset") Integer offset, @Query("type") String type);

    /**
     * 获取全部圈子
     *
     * @param limit       默认 15 ，数据返回条数 默认为15
     * @param offset      默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword     用于搜索圈子，按圈名搜索
     * @param category_id 圈子分类id
     * @return
     */
    @GET(APP_PATH_GET_ALL_CIRCLE)
    Observable<List<CircleInfo>> getAllCircle(@Query("limit") Integer limit, @Query("offset") Integer offset
            , @Query("keyword") String keyword
            , @Query("category_id") Integer category_id);

    /**
     * 获取附近圈子
     *
     * @param limit
     * @param offset
     * @param longitude
     * @param latitude
     * @return
     */
    @GET(APP_PATH_GET_ROUNDCIRCLE)
    Observable<List<CircleInfo>> getRoundCircle(@Query("limit") Integer limit, @Query("offset") int offset,
                                                @Query("longitude") String longitude, @Query("latitude") String latitude);

    /**
     * 获取圈子数量
     *
     * @return
     */
    @GET(APP_PATH_GET_CIRCLE_COUNT)
    Observable<BaseJsonV2<Integer>> getCircleCount();

    /**
     * 加入圈子
     *
     * @param circleId
     * @return
     */
    @PUT(APP_PATH_PUT_JOIN_CIRCLE)
    Observable<BaseJsonV2<Object>> joinCircle(@Path("circle_id") long circleId);

    /**
     * 退出圈子
     *
     * @return
     */
    @DELETE(APP_PATH_PUT_EXIT_CIRCLE)
    Observable<BaseJsonV2<Object>> exitCircle(@Path("circle_id") long circleId);

    /**
     * 将某个成员踢出圈子
     *
     * @return
     */
    @DELETE(APP_PATH_CANCEL_CIRCLE_MEMBERS)
    Observable<BaseJsonV2<Object>> cancleCircleMember(@Path("circle_id") long circleId, @Path("member_id") long memberId);

    /**
     * 指定/撤销圈子管理员职位
     *
     * @param circleId
     * @param memberId
     * @return
     */
    @PUT(APP_PATH_DEAL_CIRCLE_MANAGER)
    Observable<BaseJsonV2<Object>> appointCircleManager(@Path("circle_id") long circleId, @Path("member_id") long memberId);

    @DELETE(APP_PATH_DEAL_CIRCLE_MANAGER)
    Observable<BaseJsonV2<Object>> cancleCircleManager(@Path("circle_id") long circleId, @Path("member_id") long memberId);

    /**
     * 加入/移除圈子黑名单
     *
     * @param circleId
     * @param memberId
     * @return
     */
    @PUT(APP_PATH_DEAL_CIRCLE_BLACKLIST)
    Observable<BaseJsonV2<Object>> appointCircleBlackList(@Path("circle_id") long circleId, @Path("member_id") long memberId);

    @DELETE(APP_PATH_DEAL_CIRCLE_BLACKLIST)
    Observable<BaseJsonV2<Object>> cancleCircleBlackList(@Path("circle_id") long circleId, @Path("member_id") long memberId);

    /**
     * 设置圈子权限
     *
     * @return
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_SET_CIRCLE_PERMISSIONS)
    Observable<BaseJsonV2<Object>> setCirclePermissions(@Path("circle_id") long circleId, @Field("permissions[]") List<String> permissions);

    /**
     * 获取圈子详情
     *
     * @param circleId
     * @return
     */
    @GET(APP_PATH_GET_CIRCLEDETAIL)
    Observable<CircleInfo> getCircleInfo(@Path("circle_id") long circleId);

    /**
     * 获取圈子下帖子列表
     *
     * @param circleId
     * @param offset
     * @return
     */
    @GET(APP_PATH_GET_POSTLIST)
    Observable<CirclePostBean> getPostListFromCircle(@Path("circle_id") long circleId, @Query("limit") int limit, @Query("offset") int offset, @Query
            ("type") String type);


    /**
     * 获取我的帖子列表
     *
     * @param limit  默认 15 ，数据返回条数 默认为15
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type   参数 type 默认 1，   1-发布的 2- 已置顶 3-置顶待审
     * @return
     */
    @GET(APP_PATH_GET_MINE_POSTLIST)
    Observable<List<CirclePostListBean>> getMinePostList(@Query("limit") Integer limit, @Query("offset") Integer offset, @Query("type") Integer type);

    /**
     * 全部帖子列表包含搜索
     *
     * @param limit    默认 15 ，数据返回条数 默认为15
     * @param offset   默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword  搜索关键词，模糊匹配圈子名称
     * @param group_id 获取某个圈子下面的全部帖子
     * @return
     */
    @GET(APP_PATH_GET_ALL_POSTLIST)
    Observable<List<CirclePostListBean>> getAllePostList(@Query("limit") Integer limit
            , @Query("offset") Integer offset
            , @Query("keyword") String keyword
            , @Query("group_id") Long group_id);


    /**
     * 用户帖子收藏列表
     *
     * @param limit  默认 15 ，数据返回条数 默认为15
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @return
     */
    @GET(APP_PATH_GET_USER_COLLECT_POST)
    Observable<List<CirclePostListBean>> getUserCollectPostList(@Query("limit") Integer limit
            , @Query("offset") Integer offset);

    /**
     * 圈子成员角色统计
     * @param circleId
     * @return
     */
    @GET(APP_PATH_GET_GROUP_MEMBER_COUNT)
    Observable<CircleMemberCountBean> getCircleMemberCount(@Path("group_id")Long circleId);

    /**
     *
     * @param circle_id
     * @param after
     * @param limit
     * @param type 默认 all, all-所有, manager-管理员, member-成员, blacklist-黑名单, audit - 带审核
     * @param name 仅仅用于搜索
     * @return
     */
    @GET(APP_PATH_GET_CIRCLEMEMBERS)
    Observable<List<CircleMembers>> getCircleMemberList(@Path("circle_id") Long circle_id, @Query("limit") Integer limit
            , @Query("after") Integer after
            , @Query("type") String type,
                                                        @Query("name") String name);

    /**
     * 转让圈子
     *
     * @param circleId
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_ATTORN_CIRCLE)
    Observable<CircleMembers> attornCircle(@Path("circle_id") Long circleId, @Field("target") long userId);


    /**
     * 创建圈子
     *
     * @param categoryId 圈子类别id
     * @return 就是返回一个圈子
     */
    @POST(APP_PATH_CREATE_CIRCLE)
    @Multipart
    @Headers({"Accept:application/json;charset=UTF-8"})
    Observable<BaseJsonV2<CircleInfo>> createCircle(@Path("category_id") long categoryId, @Part List<MultipartBody.Part> params);

    /**
     * 获取圈子协议
     *
     * @return
     */
    @GET(APP_PATH_GET_CREATE_RULE)
    Observable<BaseJsonV2<String>> getCircleRule();

    /**
     * 修改圈子信息
     *
     * @param circleId
     * @param params
     * @return
     */
    @Multipart
    @Headers({"Accept:application/json;charset=UTF-8"})
    @POST(APP_PATH_GET_CIRCLEDETAIL)
    Observable<BaseJsonV2<CircleInfo>> updateCircle(@Path("circle_id") Long circleId, @Part List<MultipartBody.Part> params);

    /**
     * 发帖
     *
     * @param circleId 圈子id
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(APP_PATH_PUBLISH_POST)
    Observable<BaseJsonV2<CirclePostListBean>> publishPost(@Path("circle_id") long circleId, @Body RequestBody body);

    /**
     * 获取帖子详情
     *
     * @param circleId
     * @param postId
     * @return
     */
    @GET(APP_PATH_POST)
    Observable<CirclePostListBean> getPostDetail(@Path("circle_id") long circleId, @Path("post_id") long postId);

    /**
     * 获取帖子评论
     *
     * @param postId
     * @param limit
     * @param after
     * @return
     */
    @GET(APP_PATH_COMMENT_POST)
    Observable<CircleCommentZip> getPostComments(@Path("post_id") long postId, @Query("limit") int limit, @Query("after") int after);

    /**
     * 圈子收入记录
     *
     * @param circleId 圈子id
     * @param start    秒级时间戳，起始筛选时间
     * @param end      秒级时间戳，结束筛选时间
     * @param after    默认 0 ，翻页标识。
     * @param limit    默认 15 ，数据返回条数 默认为15
     * @param type     默认 all, all-全部 join-成员加入 pinned-帖子置顶
     * @return
     */
    @GET(APP_PATH_GET_CIRCLE_EARNINGLIST)
    Observable<List<CircleEarningListBean>> getCircleEarningList(@Path("circle_id") long circleId,
                                                                 @Query("start") Long start, @Query("end") Long end,
                                                                 @Query("after") Long after, @Query("limit") Long limit,
                                                                 @Query("type") String type);

    /**
     * 获取帖子点赞列表
     *
     * @param postId
     * @param limit
     * @param offset
     * @return
     */
    @GET(APP_PATH_LIKE_POST)
    Observable<List<PostDigListBean>> getPostDigList(@Path("post_id") long postId, @Query("limit") int limit, @Query("after") long offset);

    /**
     * 置顶帖子
     *
     * @param parent_id
     * @param amount
     * @param day
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_TOP_POST)
    Observable<BaseJsonV2<Integer>> stickTopPost(@Path("post_id") Long parent_id, @Field("amount") Long amount, @Field("day") Integer day);

    /**
     * 置顶帖子评论
     *
     * @param parent_id
     * @param child_id
     * @param amount
     * @param day
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_TOP_POST_COMMENT)
    Observable<BaseJsonV2<Integer>> stickTopPostComment(@Field("post_id") Long parent_id, @Path("comment_id") Long child_id, @Field("amount") Long
            amount, @Field("day") Integer day);


    /**
     * 圈主和管理员置顶帖子
     *
     * @param postId
     * @param day    天数
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_MANAGER_TOP_POST)
    Observable<BaseJsonV2<Object>> stickTopPost(@Path("post_id") Long postId, @Field("day") int day);

    /**
     * 圈主和管理员撤销置顶帖子
     *
     * @param postId
     * @return
     */
    @PATCH(APP_PATH_UNDO_TOP_POST)
    Observable<BaseJsonV2<Object>> undoTopPost(@Path("post_id") Long postId);

    /**
     * 帖子打赏
     *
     * @param postId
     * @param amount
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_REWARD_POST)
    Observable<Object> rewardPost(@Path("post_id") Long postId, @Field("amount") Long amount);

    /**
     * 帖子打赏列表
     *
     * @param post_id    帖子 id
     * @param limit      列表返回数据条数
     * @param offset     翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
     * @param order      翻页标识 排序 正序-asc 倒序 desc
     * @param order_type 排序规则 date-按时间 amount-按金额
     * @return
     */
    @GET(APP_PATH_GET_REWARD_POST_LIST)
    Observable<List<RewardsListBean>> getPostRewardList(@Path("post_id") long post_id, @Query("limit") Integer limit,
                                                        @Query("offset") Integer offset, @Query("order") String order,
                                                        @Query("order_type") String order_type);

    /**
     * 同意帖子评论置顶
     *
     * @param commentId
     * @return
     */
    @PATCH(APP_PATH_APPROVED_POST_COMMENT)
    Observable<BaseJsonV2> approvedPostTopComment(@Path("comment_id") Integer commentId);

    /**
     * 拒绝帖子评论置顶
     *
     * @param commentId
     * @return
     */
    @PATCH(APP_PATH_REFUSE_POST_COMMENT)
    Observable<BaseJsonV2> refusePostTopComment(@Path("comment_id") Integer commentId);

    /**
     * 同意帖子置顶
     *
     * @param commentId
     * @return
     */
    @PATCH(APP_PATH_APPROVED_POST)
    Observable<BaseJsonV2> approvedPostTop(@Path("post_id") Long commentId);

    /**
     * 拒绝帖子置顶
     *
     * @param commentId
     * @return
     */
    @PATCH(APP_PATH_REFUSE_POST)
    Observable<BaseJsonV2> refusePostTop(@Path("post_id") Long commentId);

    /**
     * 审核圈子加入请求
     *
     * @return
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_DEAL_CIRCLE_MEMBER_JOIN)
    Observable<BaseJsonV2> dealCircleJoin(@Field("status") Integer status, @Path("circle_id") long circleId, @Path("member_id") long memberId);

    /**
     * 获取资讯评论置顶审核列表 V2
     *
     * @return
     */
    @GET(APP_PATH_GET_TOP_POST_COMMENT)
    Observable<List<TopPostCommentListBean>> getPostReviewComment(@Query("after") Integer after, @Query("limit")
            Integer limit, @Query("group") Integer group);

    /**
     * 帖子申请置顶列表
     *
     * @return
     */
    @GET(APP_PATH_TOP_POST_LIST)
    Observable<List<TopPostListBean>> getPostReview(@Query("after") Integer after, @Query("limit")
            Integer limit, @Query("group") Long circleId);

    /**
     * 圈子待审核成员列表
     *
     * @return
     */
    @GET(APP_PATH_GET_CIRCLE_MEMBER_JOIN)
    Observable<List<TopCircleJoinReQuestBean>> getCircleJoinRequest(@Query("after") Integer after, @Query("limit")
            Integer limit);


    /*******************************************  举报  *********************************************/

    /**
     * 举报圈子
     *
     * @param groupId 圈子 id
     * @param reason  举报原因
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_CIRCLE_REPOT)
    Observable<ReportResultBean> reportCircle(@Path("group_id") String groupId, @Field("reason") String reason);

    /**
     * 举报圈子中的帖子
     *
     * @param postId 帖子 id
     * @param reason 举报原因
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_CIRCLE_POST_REPOT)
    Observable<ReportResultBean> reportCirclePost(@Path("post_id") String postId, @Field("content") String reason);

    /**
     * 举报评论
     *
     * @param commentId 评论 id
     * @param reason    举报原因
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_COMMENT_REPOT)
    Observable<ReportResultBean> reportComment(@Path("comment_id") String commentId, @Field("content") String reason);

    /**
     * 圈子举报列表
     *
     * @param groupId 圈子id
     * @param after
     * @param limit   * @param start    秒级时间戳，起始筛选时间
     * @param end     秒级时间戳，结束筛选时间
     * @param status  状态 默认全部，0-未处理 1-已处理 2-已驳回
     * @return
     */
    @GET(APP_PATH_GET_CIRCLE_REPOTS)
    Observable<List<CircleReportListBean>> getCircleReportList(@Query("group_id") Long groupId,
                                                               @Query("status") Integer status,
                                                               @Query("after") Integer after,
                                                               @Query("limit") Integer limit,
                                                               @Query("start") Long start,
                                                               @Query("end") Long end);

    /**
     * 同意举报
     *
     * @param reportId 舉報的id
     * @return
     */
    @PATCH(APP_PATH_APPROVE_CIRCLE_REPOT)
    Observable<BaseJsonV2> approvedCircleReport(@Path("report_id") Long reportId);

    /**
     * 拒绝举报
     *
     * @param reportId 舉報的id
     * @return
     */
    @PATCH(APP_PATH_REFUSE_CIRCLE_REPOT)
    Observable<BaseJsonV2> refuseCircleReport(@Path("report_id") Long reportId);
}

