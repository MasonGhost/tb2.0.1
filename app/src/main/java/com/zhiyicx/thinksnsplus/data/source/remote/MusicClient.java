package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_DETAILS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COLLECT_ABLUM_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_DETAILS;


/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicClient {

    // 获取专辑列表
    @GET(APP_PATH_MUSIC_ABLUM_LIST)
    Observable<List<MusicAlbumListBean>> getMusicList(@Query("max_id") Long max_id,
                                                                @Query("limit") Long limit);

    // 获取收藏专辑列表
    @GET(APP_PATH_MUSIC_COLLECT_ABLUM_LIST)
    Observable<List<MusicAlbumListBean>> getCollectMusicList(@Query("max_id") Long max_id,
                                                                       @Query("limit") Long limit);

    // 获取专辑详情
    @GET(APP_PATH_MUSIC_ABLUM_DETAILS)
    Observable<MusicAlbumDetailsBean> getMusicAblum(@Path("special_id") String id);

    // 获取专辑评论列表
    @GET(APP_PATH_MUSIC_ABLUM_COMMENT_LIST)
    Observable<List<MusicCommentListBean>> getAblumCommentList(@Path("special_id") String
                                                                                 music_id,
                                                                  @Query("max_id") Long max_id,
                                                                  @Query("limit") Long limit);

    // 获取歌曲详情
    @GET(APP_PATH_MUSIC_DETAILS)
    Observable<MusicDetaisBean> getMusicDetails(@Path("music_id") String music_id);

    // 获取歌曲评论列表
    @GET(APP_PATH_MUSIC_COMMENT)
    Observable<List<MusicCommentListBean>> getMusicCommentList(@Path("music_id") String
                                                                                 music_id,
                                                                         @Query("max_id") Long max_id,
                                                                         @Query("limit") Long limit);

}
