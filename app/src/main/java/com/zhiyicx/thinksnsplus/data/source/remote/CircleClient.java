package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CIRCLE_CATEGROIES;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:39
 * @Email Jliuer@aliyun.com
 * @Description 新的圈子相关接口
 */
public interface CircleClient {

    /**
     * @author Jliuer
     * @Date 17/11/27 17:07
     * @Email Jliuer@aliyun.com
     * @param limit 返回条数 默认为15
     * @param offet 翻页偏移量
     * @Description 获取圈子分类
     */
    @GET(APP_PATH_GET_CIRCLE_CATEGROIES)
    Observable<List<CircleTypeBean>> getCategroiesList(@Query("limit") int limit,@Query("offet") int offet);
}
