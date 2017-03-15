package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_FOLLOW_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TYPE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_LOGIN;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoMainClient {

    @GET(APP_PATH_INFO_TYPE)
    Observable<BaseJson<InfoTypeBean>> getInfoType();

    @GET(APP_PATH_INFO_LIST)
    Observable<BaseJson<List<InfoListBean>>> getInfoList(@Query("cate_id") String cate_id,
                                                         @Query("max_id") Long max_id,
                                                         @Query("limit") Long limit,
                                                         @Query("page") Long page);
    @FormUrlEncoded
    @POST(APP_PATH_INFO_FOLLOW_LIST)
    Observable<BaseJson<Integer>> doSubscribe(@Field("follows") String follows);
}
