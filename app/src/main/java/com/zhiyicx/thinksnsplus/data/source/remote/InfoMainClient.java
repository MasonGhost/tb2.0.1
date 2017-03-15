package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TYPE;

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
    Observable<BaseJson<InfoListBean>> getInfoList(@Query("cate_id") String cate_id,
                                                         @Query("max_id") long max_id,
                                                         @Query("limit") long limit,
                                                         @Query("page") long page);
}
