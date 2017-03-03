package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe 动态相关的接口
 * @date 2017/2/21
 * @contact email:450127106@qq.com
 */

public interface DynamicClient {
    /**
     * 发布动态
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(ApiConfig.APP_PATH_SEND_DYNAMIC)
    Observable<BaseJson<Object>> sendDynamic(@Body RequestBody body);

    /**
     * 获取动态列表
     *
     * @param type   "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param max_id 用来翻页的记录id(对应数据体里的feed_id ,最新和关注选填)
     * @param limit  请求数据条数 默认10条
     * @param page   页码 热门选填
     * @return dynamic list
     */
    @GET(ApiConfig.APP_PATH_GET_DYNAMIC_LIST)
    Observable<BaseJson<List<DynamicBean>>> getDynamicList(@Path("type") String type, @Query("max_id") Long max_id, @Query("limit") Long limit, @Query("page") int page);

    /**
     * #点赞一条动态
     *
     * @param feed_id 动态的 id
     * @return
     */
    @POST(ApiConfig.APP_PATH_DYNAMIC_HANDLE_LIKE)
    Observable<BaseJson<String>> likeDynamic(@Path("feed_id") Long feed_id);

    /**
     * #取消点赞一条动态
     *
     * @param feed_id 动态的 id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_DYNAMIC_HANDLE_LIKE)
    Observable<BaseJson<String>> cancleLikeDynamic(@Path("feed_id") Long feed_id);

    /**
     * 获取某条动态的点赞用户列表
     *
     * @param feed_id 动态的唯一id
     * @param max_id  返回的feed_digg_id 作为max_id,对象为null表示不传
     * @return
     */
    @GET(ApiConfig.APP_PATH_DYNAMIC_DIG_LIST)
    Observable<BaseJson<List<DynamicDigListBean>>> getDynamicDigList(@Path("feed_id") Long feed_id, @Query("max_id ") Integer max_id);

    /**
     * 收藏动态
     */
    @POST(ApiConfig.APP_PATH_HANDLE_COLLECT)
    Observable<BaseJson<Object>> collectDynamic(@Path("feed_id") Long feed_id);

    /**
     * 取消动态收藏
     *
     * @param feed_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_HANDLE_COLLECT)
    Observable<BaseJson<Object>> cancleCollectDynamic(@Path("feed_id") Long feed_id);
}
