package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicListBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public interface MusicClient {
    /**
     *
     * @param maps
     * @return
     */
    @GET
    @PATCH("tym")
    Observable<BaseJson<MusicListBean>> getMusicList(@QueryMap Map<String, String> maps);
}
