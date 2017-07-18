package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

import java.util.List;

import javax.inject.Inject;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface ChannelClient {
    /**
     * 订阅某个频道
     */
    @POST(ApiConfig.APP_PATH_HANDLE_SUBSCRIB_CHANNEL)
    Observable<BaseJson<Object>> subscribChannel(@Path("channel_id") long channel_id);

    /**
     * 取消某个频道的订阅
     */
    @DELETE(ApiConfig.APP_PATH_HANDLE_SUBSCRIB_CHANNEL)
    Observable<BaseJson<Object>> cancleSubscribChannel(@Path("channel_id") long channel_id);

    /**
     * 获取频道列表
     *
     * @param type 频道类型 “”表示所有的频道  “my”表示我关注的频道
     */
    @GET(ApiConfig.APP_PATH_GET_CHANNEL)
    Observable<BaseJson<List<ChannelInfoBean>>> getChannelList(@Path("type") String type);

    /**
     * 获取频道的动态列表
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_DYNAMIC_LIST)
    Observable<List<GroupDynamicListBean>> getDynamicListFromGroup(@Path("group") long goup_id,
                                                                   @Query("limit") int limit, @Query("after") long max_id);

    /**
     * 获取所有圈子列表的接口
     *
     * @param limit  限制条数
     * @param max_id max_id
     */
    @GET(ApiConfig.APP_PATH_GET_ALL_GROUP)
    Observable<List<GroupInfoBean>> getAllGroupList(@Query("limit") int limit, @Query("after") long max_id);

    /**
     * 获取用户加入的圈子列表
     *
     * @param limit  限制条数
     * @param max_id max_id
     */
    @GET(ApiConfig.APP_PATH_GET_USER_JOINED_GROUP)
    Observable<List<GroupInfoBean>> getUserJoinedGroupList(@Query("limit") int limit, @Query("after") long max_id);

    /**
     * 加入圈子
     *
     * @param groupId 圈子id
     */
    @POST(ApiConfig.APP_PATH_JOIN_GROUP)
    Observable<BaseJsonV2<Object>> joinGroup(@Path("group") long groupId);

    /**
     * 退出圈子
     *
     * @param groupId 圈子id
     */
    @DELETE(ApiConfig.APP_PATH_JOIN_GROUP)
    Observable<BaseJsonV2<Object>> quitGroup(@Path("group") long groupId);
}
