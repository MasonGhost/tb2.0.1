package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_BATCH_SPECIFIED_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CURRENT_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_IM_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_SPECIFIED_USER_INFO;
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
    Observable<BaseJson> changeUserInfo(@FieldMap HashMap<String, Object> userFieldMap);

    /**
     * 获取用户信息  v1 版本
     *
     * @return
     */
    @POST(APP_PATH_GET_USER_INFO)
    Observable<BaseJson<List<UserInfoBean>>> getUserInfo(@Body RequestBody requestBody);

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @GET(APP_PATH_GET_CURRENT_USER_INFO)
    Observable<UserInfoBean> getCurrentLoginUserInfo();

    /**
     * 获取指定用户信息  其中 following、follower 是可选参数，验证用户我是否关注以及是否关注我的用户 id ，默认为当前登陆用户。
     *
     * @param userId          the specified user id
     * @param followingUserId following user id
     * @param followerUserId  follow user id
     * @return
     */
    @GET(APP_PATH_GET_SPECIFIED_USER_INFO)
    Observable<UserInfoBean> getSpecifiedUserInfo(@Path("user_id") long userId, @Query("following") Long followingUserId, @Query("follower") Long followerUserId);

    /**
     * 批量获取用户信息
     *
     * @param user_ids Get multiple designated users, multiple IDs using , split.
     * @param name     Used to retrieve users whose username contains name.
     * @param since    The integer ID of the last User that you've seen.
     * @param order    Sorting. Enum: asc, desc
     * @param limit    List user limit, minimum 1 max 50.
     * @return
     */
    @GET(APP_PATH_GET_BATCH_SPECIFIED_USER_INFO)
    Observable<List<UserInfoBean>> getBatchSpecifiedUserInfo(@Query("id") String user_ids, @Query("name") String name, @Query("since") Integer since, @Query("order") String order, @Query("limit") Integer limit);

    /**
     * 获取 IM 信息
     *
     * @return
     */
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
    Observable<BaseJson<List<DigRankBean>>> getDigRankList(@Query("page") int page,
                                                           @Query("limit") int limit);


    /**
     * 获取用户收到的点赞
     *
     * @param after 用来翻页数据体记录 id
     * @param limit 返回数据条数 默认 20 条
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_DIGGS)
    Observable<List<DigedBean>> getMyDiggs(@Query("after") int after,
                                           @Query("limit") int limit);

    /**
     * 获取用户收到的评论
     *
     * @param after 用来翻页数据体记录 id
     * @param limit 返回数据条数 默认 20 条
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_COMMENTS)
    Observable<List<CommentedBean>> getMyComments(@Query("after") int after,
                                                  @Query("limit") int limit);

    /**
     * 获取用户收到的评论
     *
     * @param time 零时区的秒级时间戳
     * @param key  查询关键字 默认查询全部 多个以逗号隔开 可选参数有 diggs comments follows
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_FLUSHMESSAGES)
    Observable<BaseJson<List<FlushMessages>>> getMyFlushMessages(@Query("time") long time,
                                                                 @Query("key") String key);

    /**
     * 未读通知数量检查
     *
     * @return
     * @see {https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md#%E9%80%9A%E7%9F%A5%E5%88%97%E8%A1%A8}
     */
    @HEAD(ApiConfig.APP_PATH_GET_CKECK_UNREAD_NOTIFICATION)
    Observable<Void> ckeckUnreadNotification();

    /**
     * 通知列表
     *
     * @param notification 检索具体通知，可以是由 , 拼接的 IDs 组，也可以是 Array
     * @param type         获取通知类型，可选 all,read,unread 默认 all
     * @param limit        获取条数，默认 20
     * @param offset       数据偏移量，默认 0
     * @return
     * @see {https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md#%E9%80%9A%E7%9F%A5%E5%88%97%E8%A1%A8}
     */
    @GET(ApiConfig.APP_PATH_GET_NOTIFICATION_LIST)
    Observable<List<TSPNotificationBean>> getNotificationList(@Query("notification") String notification, @Query("type") String type, @Query("limit") Integer limit, @Query("offset") Integer offset);

    /**
     * 读取通知
     *
     * @param notificationId
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_NOTIFICATION_DETIAL)
    Observable<TSPNotificationBean> getNotificationDetail(@Path("notification") String notificationId);

    /**
     * 标记通知阅读
     *
     * @param notificationId 通知ID，可以是由 , 拼接的 IDs 组，也可以是 Array
     * @return
     */
    @PATCH(ApiConfig.APP_PATH_MAKE_NOTIFICAITON_READED)
    Observable<Object> makeNotificationReaded(@Query("notification") String notificationId);

    /**
     * 更新用户头像
     *
     * @param multipartBody
     * @return
     */
    @POST(ApiConfig.APP_PATH_UPDATE_USER_AVATAR)
    Observable<Object> updateAvatar(@Body MultipartBody multipartBody);

    /**
     * 更新用户背景
     *
     * @param multipartBody
     * @return
     */
    @POST(ApiConfig.APP_PATH_UPDATE_USER_BG)
    Observable<Object> updateBg(@Body MultipartBody multipartBody);
}
