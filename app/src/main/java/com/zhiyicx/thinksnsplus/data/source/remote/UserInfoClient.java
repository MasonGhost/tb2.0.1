package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.request.BindAccountRequstBean;
import com.zhiyicx.thinksnsplus.data.beans.request.DeleteUserPhoneOrEmailRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.ThirdAccountBindRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.UpdateUserPhoneOrEmailRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBShareLinkBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskRewardRuleBean;
import com.zhiyicx.thinksnsplus.modules.tb.contract.ContractData;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.modules.tb.mechainism.MerchainInfo;
import com.zhiyicx.thinksnsplus.modules.tb.rank.RankData;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_BIND_WITH_INPUT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_BIND_WITH_LOGIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CANDEL_BIND;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_BIND_OR_GET_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_IN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_REGISTER_OR_GET_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_BATCH_SPECIFIED_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_BIND_THIRDS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_BY_PHONE_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CHECK_IN_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CHECK_IN_RANKS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CURRENT_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_HOT_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_IM_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_IM_INFO_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_NEW_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_RECOMMENT_BY_TAG_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_RECOMMENT_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_SPECIFIED_USER_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_USER_AROUND;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REPORT_USER;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REWARD_USER;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SEARCH_RECOMMENT_USER;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_UPDATE_USER_LOCATION;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST;

/**
 * @author Jungle68
 * @describe 用户信息相关的网络请求
 * @date 2017/1/9
 * @contact email:master.jungle68@gmail.com
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
    Observable<Object> changeUserInfo(@FieldMap HashMap<String, Object> userFieldMap);


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
    Observable<UserInfoBean> getSpecifiedUserInfo(@Path("user_id") long userId, @Query("following") Long followingUserId, @Query("follower") Long
            followerUserId);

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
    Observable<List<UserInfoBean>> getBatchSpecifiedUserInfo(@Query("id") String user_ids, @Query("name") String name, @Query("since") Integer
            since, @Query("order") String order, @Query("limit") Integer limit);

    /**
     * 获取 IM 信息
     *
     * @return
     */
    @GET(APP_PATH_GET_IM_INFO)
    Observable<IMBean> getIMInfo();

    @GET(APP_PATH_GET_IM_INFO_V2)
    Observable<IMBean> getIMInfoV2();

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
     * 未读通知数量检查
     *
     * @return
     * @see {https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md#%E9%80%9A%E7%9F%A5%E5%88%97%E8%A1%A8}
     */
    @HEAD(ApiConfig.APP_PATH_GET_CKECK_UNREAD_NOTIFICATION)
    Observable<Void> ckeckUnreadNotification();


    /**
     * 获取用户未读消息
     *
     * @return
     * @see {https://slimkit.github.io/plus-docs/v2/core/users/unread#用户未读消息}
     */
    @GET(ApiConfig.APP_PATH_GET_UNREAD_NOTIFICATION)
    Observable<UnReadNotificaitonBean> getUnreadNotificationData();

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
    Observable<List<TSPNotificationBean>> getNotificationList(@Query("notification") String notification, @Query("type") String type, @Query
            ("limit") Integer limit, @Query("offset") Integer offset);

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
     * 标记所有通知阅读
     *
     * @return
     */
    @PUT(ApiConfig.APP_PATH_MAKE_NOTIFICAITON_ALL_READED)
    Observable<Object> makeNotificationAllReaded();

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


    /**
     * 更新认证用户的手机号码和邮箱
     *
     * @param updateUserPhoneOrEmailRequestBean request data
     * @return
     */
    @PUT(ApiConfig.APP_PATH_UPDATE_USER_PHONE_OR_EMAIL)
    Observable<Object> updatePhoneOrEmail(@Body UpdateUserPhoneOrEmailRequestBean updateUserPhoneOrEmailRequestBean);

    /**
     * 解除用户 Phone 绑定:
     *
     * @param deleteUserPhoneOrEmailRequestBean
     * @return
     */
    @HTTP(method = "DELETE", path = ApiConfig.APP_PATH_DELETE_USER_PHONE, hasBody = true)
    Observable<Object> deletePhone(@Body DeleteUserPhoneOrEmailRequestBean deleteUserPhoneOrEmailRequestBean);

    /**
     * 解除用户 E-Mail 绑定:
     *
     * @param deleteUserPhoneOrEmailRequestBean
     * @return
     */
    @HTTP(method = "DELETE", path = ApiConfig.APP_PATH_DELETE_USER_EMAIL, hasBody = true)
    Observable<Object> deleteEmail(@Body DeleteUserPhoneOrEmailRequestBean deleteUserPhoneOrEmailRequestBean);


    /*******************************************  标签  *********************************************/


    /**
     * 获取一个用户的标签
     *
     * @param user_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_USER_TAGS)
    Observable<List<UserTagBean>> getUserTags(@Path("user_id") long user_id);

    /**
     * 获取当前认证用户的标签
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_CURRENT_USER_TAGS)
    Observable<List<UserTagBean>> getCurrentUserTags();

    /**
     * 当前认证用户附加一个标签
     *
     * @param tag_id
     * @return
     */
    @PUT(ApiConfig.APP_PATH_CURRENT_USER_ADD_TAGS)
    Observable<Object> addTag(@Path("tag_id") long tag_id);

    /**
     * 当前认证用户分离一个标签
     *
     * @param tag_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_CURRENT_USER_DELETE_TAGS)
    Observable<Object> deleteTag(@Path("tag_id") long tag_id);

    /*******************************************  认证  *********************************************/

    /**
     * 获取用户认证信息
     */
    @GET(ApiConfig.APP_PATH_CERTIFICATION)
    Observable<UserCertificationInfo> getUserCertificationInfo();

    /**
     * 提交认证信息
     */
    @POST(ApiConfig.APP_PATH_CERTIFICATION)
    Observable<BaseJsonV2<Object>> sendUserCertificationInfo(@Body RequestBody requestBody);

    /**
     * 更新认证信息
     */
    @PATCH(ApiConfig.APP_PATH_CERTIFICATION)
    Observable<BaseJsonV2<Object>> updateUserCertificationInfo(@Body RequestBody requestBody);

    /*******************************************  打赏  *********************************************/

    /**
     * 打赏一个用户
     *
     * @param user_id target user
     * @param amount  reward amount 真实货币的分单位
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_REWARD_USER)
    Observable<Object> rewardUser(@Path("user_id") long user_id, @Field("amount") long amount);

    /*******************************************  找人  *********************************************/

    /**
     * 热门用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @GET(APP_PATH_GET_HOT_USER_INFO)
    Observable<List<UserInfoBean>> getHotUsers(@Query("limit") Integer limit, @Query("offset") Integer offset);

    /**
     * 最新用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @GET(APP_PATH_GET_NEW_USER_INFO)
    Observable<List<UserInfoBean>> getNewUsers(@Query("limit") Integer limit, @Query("offset") Integer offset);

    /**
     * tag 推荐用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @GET(APP_PATH_GET_RECOMMENT_BY_TAG_USER_INFO)
    Observable<List<UserInfoBean>> getUsersRecommentByTag(@Query("limit") Integer limit, @Query("offset") Integer offset);

    /**
     * @return 后台推荐用户 最多200
     */
    @GET(APP_PATH_GET_RECOMMENT_USER_INFO)
    Observable<List<UserInfoBean>> getRecommendUserInfo();

    /**
     * 搜索用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @GET(APP_PATH_SEARCH_RECOMMENT_USER)
    Observable<List<UserInfoBean>> searchUserinfoWithRecommend(@Query("limit") Integer limit, @Query("offset") Integer offset, @Query("keyword")
            String keyword);

    /**
     * phone 推荐用户
     * <p>
     * { "phones": [ 18877778888, 18999998888, 17700001111 ] }
     *
     * @return
     */
    @POST(APP_PATH_GET_BY_PHONE_USER_INFO)
    Observable<List<UserInfoBean>> getUsersByPhone(@Body RequestBody phones);

    /**
     * 更新位置数据
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_UPDATE_USER_LOCATION)
    Observable<Object> updateUserLocation(@Field("longitude") double longitude, @Field("latitude") double latitude);

    /**
     * 根据经纬度查询周围最多 50KM 内的 TS+ 用户
     *
     * @param longitude 当前用户所在位置的纬度
     * @param latitude  当前用户所在位置的经度
     * @param radius    搜索范围，米为单位 [0 - 50000], 默认3000
     * @param limit     默认20， 最大100
     * @param page      分页参数， 默认1，当返回数据小于limit， page达到最大值
     * @return
     */
    @GET(APP_PATH_GET_USER_AROUND)
    Observable<List<NearbyBean>> getNearbyData(@Query("longitude") double longitude, @Query("latitude") double latitude, @Query("radius") Integer
            radius, @Query("limit") Integer limit, @Query("page") Integer page);

    /*******************************************  签到  *********************************************/

    /**
     * 获取签到信息
     *
     * @return
     */
    @GET(APP_PATH_GET_CHECK_IN_INFO)
    Observable<CheckInBean> getCheckInInfo();

    /**
     * 签到
     *
     * @return
     */
    @POST(APP_PATH_CHECK_IN)
    Observable<Object> checkIn();

    /**
     * 连续签到排行榜
     *
     * @param offset 数据偏移数，默认为 0。
     * @return
     */
    @GET(APP_PATH_GET_CHECK_IN_RANKS)
    Observable<List<UserInfoBean>> getCheckInRanks(@Query("offset") Integer offset);

    /*******************************************  三方登录  *********************************************/

    /**
     * 获取已经绑定的三方
     * qq	    腾讯 QQ 。
     * weibo	新浪 Weibo 。
     * wechat	腾讯微信 。
     *
     * @return 请求成功后，将返回用户已绑定第三方的 provider 名称，不存在列表中的代表用户并为绑定。
     */
    @GET(APP_PATH_GET_BIND_THIRDS)
    Observable<List<String>> getBindThirds();

    /**
     * 检查绑定并获取用户授权
     *
     * @param access_token thrid token
     * @return 返回的数据参考 「用户／授权」接口，如果返回 404 则表示没有改账号没有注册，进入第三方登录注册流程。
     */
    @FormUrlEncoded
    @POST(APP_PATH_CHECK_BIND_OR_GET_USER_INFO)
    Observable<AuthBean> checkThridIsRegitser(@Path("provider") String provider, @Field("access_token") String access_token);

    /**
     * 检查注册信息或者注册用户
     *
     * @param provider      type qq\weibo\wechat
     * @param thridInfoBean 获取的 Provider Access Token。
     * @param thridInfoBean 用户名。
     * @param thridInfoBean 如果是 null 、 false 或 0 则不会进入检查，如果 存在任何转为 bool 为 真 的值，则表示检查注册信息。
     * @return
     */
    @PATCH(APP_PATH_CHECK_REGISTER_OR_GET_USER_INFO)
    Observable<AuthBean> checkUserOrRegisterUser(@Path("provider") String provider, @Body ThridInfoBean thridInfoBean);

    /**
     * 已登录账号绑定
     *
     * @param provider
     * @param access_token
     * @return
     */
    @PATCH(APP_PATH_BIND_WITH_LOGIN)
    Observable<Object> bindWithLogin(@Path("provider") String provider, @Body ThirdAccountBindRequestBean access_token);

    /**
     * 输入账号密码绑定
     *
     * @param provider              type qq\weibo\wechat
     * @param bindAccountRequstBean 获取的 Provider Access Token。
     * @param bindAccountRequstBean 用户登录名，手机，邮箱
     * @param bindAccountRequstBean 用户密码。
     * @return
     */
    @PUT(APP_PATH_BIND_WITH_INPUT)
    Observable<AuthBean> bindWithInput(@Path("provider") String provider,
                                       @Body BindAccountRequstBean bindAccountRequstBean);

    /**
     * 取消绑定
     *
     * @param provider type qq\weibo\wechat
     * @return
     */
    @DELETE(APP_PATH_CANDEL_BIND)
    Observable<Object> cancelBind(@Path("provider") String provider);

    /**
     * 举报用户，举报信息只有管理员在后台可以看到
     *
     * @param userId 用户 id
     * @param reason 举报原因
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_REPORT_USER)
    Observable<ReportResultBean> reportUser(@Path("user_id") String userId, @Field("reason") String reason);


    /*******************************************  TB  *********************************************/

    /**
     * 财富排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_TB)
    Observable<List<RankData>> getTBRank(@Query("limit") Long limit,
                                         @Query("offset") int size);

    /**
     * @param limit limit	int	条目数
     * @param size  offset	int	翻页标示
     * @param type  type	string	默认: all, all-累计贡献排行 day-日贡献排行
     * @return
     */
    @GET(ApiConfig.APP_PATH_RANK_TB_CONTRUBITHION)
    Observable<List<ContributionData>> getContributionRank(@Query("limit") Long limit,
                                                           @Query("offset") int size, @Query("type") String type);

    /**
     * 获取用户关注的机构
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_USER_FOLLOWING)
    Observable<List<ContractData>> getContract();

    /**
     * @param name  user	user  	string    	yes    	用户名模糊匹配
     * @param limit limit	int	条目数
     * @param size  offset	int	翻页标示
     * @param type  type	string	默认: all, all-累计贡献排行 day-日贡献排行
     * @return
     */
    @GET(ApiConfig.APP_PATH_SEARCH_MERCHANSIM_USER)
    Observable<List<UserInfoBean>> searchMerchainsimUser(@Query("limit") Long limit,
                                                         @Query("offset") int size, @Query("user") String name, @Query("type") String type);

    /**
     * 分享统计
     *
     * @param type type	string	非必须, 快讯分享需传:feed
     * @param id   id	int	非必须, 分享资源id
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_SHARE_DYNAMIC_COUNT)
    Observable<BaseJsonV2> shareCount(@Field("type") String type, @Field("id") String id);

    /**
     * 获取
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_TASTK_INFO)
    Observable<TBTaskContainerBean> getTaskInfo();

    /**
     * 获取任务奖励说明
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_TASTK_REWARD_RULE)
    Observable<TBTaskRewardRuleBean> getTaskRewardRule();

    /**
     * @param limit
     * @param after
     * @param action income - 收入 expenses - 支出
     * @return
     */
    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST)
    Observable<List<RechargeSuccessBean>> getRechargeSuccessList(@Query("limit") int limit, @Query("after") int after, @Query("action") String
            action);

    /**
     * 获取任务奖励说明
     *
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.APP_PATH_SUBMIT_INVITE_CODE)
    Observable<BaseJsonV2> submitInviteCode(@Field("user_id") String user_id);

    /**
     * 修改是否参与排名
     */
    @POST(ApiConfig.APP_PATH_CHANGE_RANK_STATUS)
    Observable<String> changeRankStatus();

    /**
     * 获取是否排名状态
     */
    @GET(ApiConfig.APP_PATH_GET_RANK_STATUS)
    Observable<String> getRankStatus();

    /**
     * 获取机构信息
     *
     * @param user_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_SEARCH_MERCHANSIM_INFO)
    Observable<MerchainInfo> getMerchainUserInfo(@Query("user_id") int user_id);

    /**
     * 分享连接
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_SHARE_LINK_URL)
    Observable<TBShareLinkBean> getShareLink();

}
