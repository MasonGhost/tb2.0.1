package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CirclePostBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CircleCommentZip;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_COMMENT_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CREATE_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ALL_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLEDETAIL;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLELIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_CATEGROIES;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_COUNT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_MINE_POSTLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_MY_JOINED_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_POSTLIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_RECOMMEND_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ROUNDCIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LIKEREWARD_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LIKE_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_PUBLISH_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_PUT_EXIT_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_PUT_JOIN_CIRCLE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SET_CIRCLE_PERMISSIONS;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:39
 * @Email Jliuer@aliyun.com
 * @Description 新的圈子相关接口
 */
public interface CircleClient {

    /**
     * 我加入的圈子接口
     * 默认: join, join 我加入 audit 待审核
     */
    enum MineCircleType {
        JOIN("join"),
        AUDIT("audit"),
        SEARCH("search");
        public String value;

        MineCircleType(String value) {
            this.value = value;
        }
    }

    /**
     * @param limit 返回条数 默认为15
     * @param offet 翻页偏移量
     * @author Jliuer
     * @Date 17/11/27 17:07
     * @Email Jliuer@aliyun.com
     * @Description 获取圈子分类
     */
    @GET(APP_PATH_GET_CIRCLE_CATEGROIES)
    Observable<List<CircleTypeBean>> getCategroiesList(@Query("limit") int limit, @Query("offet") int offet);

    /**
     * 获取圈子列表
     *
     * @param categoryId 圈子类别id
     * @param limit
     * @param offet
     * @return
     */
    @GET(APP_PATH_GET_CIRCLELIST)
    Observable<List<CircleInfo>> getCircleList(@Path("category_id") long categoryId, @Query("limit") int limit, @Query("offet") int offet);

    /**
     * 获取推荐的圈子
     *
     * @param limit
     * @param offet
     * @return
     */
    @GET(APP_PATH_GET_RECOMMEND_CIRCLE)
    Observable<List<CircleInfo>> getRecommendCircle(@Query("limit") int limit, @Query("offet") int offet);

    /**
     * 获取已经加入的圈子
     *
     * @param limit 默认 20 ，数据返回条数 默认为20
     * @param offet 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type  默认: join, join 我加入 audit 待审核
     * @return
     */
    @GET(APP_PATH_GET_MY_JOINED_CIRCLE)
    Observable<List<CircleInfo>> getMyJoinedCircle(@Query("limit") Integer limit, @Query("offet") Integer offet, @Query("type") String type);

    /**
     * 获取全部圈子
     *
     * @param limit       默认 15 ，数据返回条数 默认为15
     * @param offet       默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword     用于搜索圈子，按圈名搜索
     * @param category_id 圈子分类id
     * @return
     */
    @GET(APP_PATH_GET_ALL_CIRCLE)
    Observable<List<CircleInfo>> getAllCircle(@Query("limit") Integer limit, @Query("offet") Integer offet
            , @Query("keyword") String keyword
            , @Query("category_id") Integer category_id);

    /**
     * 获取附近圈子
     *
     * @param limit
     * @param offet
     * @param longitude
     * @param latitude
     * @return
     */
    @GET(APP_PATH_GET_ROUNDCIRCLE)
    Observable<List<CircleInfo>> getRoundCircle(@Query("limit") Integer limit, @Query("offet") int offet,
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
     * 设置圈子权限
     *
     * @return
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_SET_CIRCLE_PERMISSIONS)
    Observable<Object> setCirclePermissions();

    /**
     * 获取圈子详情
     *
     * @param circleId
     * @return
     */
    @GET(APP_PATH_GET_CIRCLEDETAIL)
    Observable<CircleInfoDetail> getCircleInfoDetail(@Path("circle_id") long circleId);

    /**
     * 获取圈子下帖子列表
     *
     * @param circleId
     * @param offet
     * @return
     */
    @GET(APP_PATH_GET_POSTLIST)
    Observable<CirclePostBean> getPostListFromCircle(@Path("circle_id") long circleId, @Query("limit") int limit, @Query("offet") int offet);

    /**
     * 获取我的帖子列表
     *
     * @param limit
     * @param offet
     * @param type  参数 type 默认 1，   1-发布的 2- 已置顶 3-置顶待审
     * @return
     */
    @GET(APP_PATH_GET_MINE_POSTLIST)
    Observable<List<CirclePostListBean>> getMinePostList(@Query("limit") int limit, @Query("offet") int offet, @Query("type") int type);

    /**
     * 创建圈子
     *
     * @param categoryId 圈子类别id
     * @param params     参数哟
     * @return 就是返回一个圈子
     */
    @POST(APP_PATH_CREATE_CIRCLE)
    @Multipart
    Observable<BaseJsonV2<CircleInfo>> createCircle(@Path("category_id") long categoryId, @Part List<MultipartBody.Part> params);

    /**
     * 发帖
     *
     * @param circleId 圈子id
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(APP_PATH_PUBLISH_POST)
    Observable<BaseJsonV2<Object>> publishPost(@Path("circle_id") long circleId, @Body RequestBody body);

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
     * @param offet
     * @return
     */
    @GET(APP_PATH_COMMENT_POST)
    Observable<CircleCommentZip> getPostComments(@Path("post_id") long postId, @Query("limit") int limit, @Query("after") int offet);

    /**
     * 获取帖子点赞列表
     *
     * @param postId
     * @param limit
     * @param offet
     * @return
     */
    @GET(APP_PATH_LIKE_POST)
    Observable<List<PostDigListBean>> getPostDigList(@Path("post_id") long postId, @Query("limit") int limit, @Query("after") long offet);

    /**
     * 获取帖子打赏列表
     *
     * @param postId
     * @param limit
     * @param offet
     * @return
     */
    @GET(APP_PATH_LIKEREWARD_POST)
    Observable<List<RewardsListBean>> getPostRewardList(@Path("post_id") long postId, @Query("limit") int limit, @Query("after") long offet);
}
