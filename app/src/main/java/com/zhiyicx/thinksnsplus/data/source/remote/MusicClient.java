package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AblumCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_COLLECT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_DETAILS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_DETAILS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_DIGG;


/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicClient {

    // 获取专辑列表
    @GET(APP_PATH_MUSIC_ABLUM_LIST)
    Observable<BaseJson<List<MusicAlbumListBean>>> getMusicList(@Query("max_id") Long max_id,
                                                                @Query("limit") Long limit);

    // 获取专辑详情
    @GET(APP_PATH_MUSIC_ABLUM_DETAILS)
    Observable<BaseJson<MusicAlbumDetailsBean>> getMusicAblum(@Path("special_id") String id);

    // 获取专辑评论列表
    @GET(APP_PATH_MUSIC_ABLUM_COMMENT)
    Observable<BaseJson<List<AblumCommentListBean>>> getAblumCommentList(@Path("music_id") String
                                                                                 music_id);

    // 获取歌曲详情
    @GET(APP_PATH_MUSIC_DETAILS)
    Observable<BaseJson<MusicDetaisBean>> getMusicDetails(@Path("music_id") String music_id);

    // 获取歌曲评论列表
    @GET(APP_PATH_MUSIC_COMMENT)
    Observable<BaseJson<List<MusicCommentListBean>>> getMusicCommentList(@Path("music_id") String
                                                                                 music_id,
                                                                         @Query("max_id") Long max_id,
                                                                         @Query("limit") Long limit);

    // 评论专辑
    @POST(APP_PATH_MUSIC_ABLUM_COMMENT)
    Observable<BaseJson<Integer>> commentAblum(@Path("special_id") String special_id);

    // 评论歌曲
    @POST(APP_PATH_MUSIC_COMMENT)
    Observable<BaseJson<Integer>> commentMusic(@Path("music_id") String music_id);

    // 点赞歌曲
    @POST(APP_PATH_MUSIC_DIGG)
    Observable<BaseJson<Integer>> doDigg(@Path("music_id") String music_id);

    // 歌曲取消点赞
    @DELETE(APP_PATH_MUSIC_DIGG)
    Observable<BaseJson<Integer>> cancleDigg(@Path("music_id") String music_id);

    // 收藏专辑
    @POST(APP_PATH_MUSIC_ABLUM_COLLECT)
    Observable<BaseJson<Integer>> collectAblum(@Path("special_id") String special_id);

    // 取消收藏专辑
    @DELETE(APP_PATH_MUSIC_ABLUM_COLLECT)
    Observable<BaseJson<Integer>> cancelCollectAblum(@Path("special_id") String special_id);
}
