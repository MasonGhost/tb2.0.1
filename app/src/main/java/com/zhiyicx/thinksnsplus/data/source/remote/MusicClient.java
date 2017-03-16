package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_DETAILS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_LIST;


/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public interface MusicClient {

    @GET(APP_PATH_MUSIC_ABLUM_LIST)
    Observable<BaseJson<List<MusicAlbumListBean>>> getMusicList(@Query("max_id") Long max_id,
                                                                @Query("limit") Long limit);

    @GET(APP_PATH_MUSIC_ABLUM_DETAILS)
    Observable<BaseJson<MusicAlbumDetailsBean>> getMusicAblum(@Path("special_id") String id);
}
