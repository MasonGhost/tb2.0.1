package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoWebBean;

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
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_SEARCH;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TYPE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LOGIN;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoMainClient {

    // 获取资讯分类
    @GET(APP_PATH_INFO_TYPE)
    Observable<InfoTypeBean> getInfoType();
//    Observable<BaseJson<InfoTypeBean>> getInfoType();

    // 获取资讯分类
    @GET(APP_PATH_INFO_DETAILS)
    Observable<BaseJson<InfoWebBean>> getInfoWebContent(@Path("news_id")String news_id);

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


}
