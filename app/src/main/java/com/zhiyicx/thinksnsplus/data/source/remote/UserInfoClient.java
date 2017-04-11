package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DigBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_IM_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_USER_INFO;

/**
 * @author LiuChao
 * @describe 用户信息相关的网络请求
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public interface UserInfoClient {
    // 上传头像功能，写在CommonClient中

    /**
     * 修改用户资料
     *
     * @param userFieldMap 用户需要修改哪那些信息不确定
     */
    @FormUrlEncoded
    @PATCH(APP_PATH_CHANGE_USER_INFO)
    Observable<BaseJson> changeUserInfo(@FieldMap HashMap<String, String> userFieldMap);

    /**
     * 获取用户信息
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(APP_PATH_GET_USER_INFO)
    Observable<BaseJson<List<UserInfoBean>>> getUserInfo(@Body RequestBody requestBody);


    @GET(APP_PATH_GET_IM_INFO)
    Observable<BaseJson<IMBean>> getIMInfo();

    /**
     * 获取用户关注状态
     *
     * @param user_ids 多个用户 id 通过“ ，”来隔开
     */
    @GET(ApiConfig.APP_PATH_GET_USER_FOLLOW_STATE)
    Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(@Query("user_ids") String user_ids);


    /**
     * 用户点赞排行
     *
     * @param page  页码 默认为 1
     * @param limit 返回数据条数 默认15条
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_DIGGS_RANK)
    Observable<BaseJson<List<DigBean>>> getRankList(@Query("page") Long page,
                                                    @Query("limit") Long limit);


}
