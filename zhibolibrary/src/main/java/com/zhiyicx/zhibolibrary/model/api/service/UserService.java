package com.zhiyicx.zhibolibrary.model.api.service;

import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.model.entity.WalletStatus;

import okhttp3.FormBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jess on 16/4/19.
 */
public interface UserService {
    public static final int STATUS_FOLLOW = 1;
    public static final int STATUS_FOLLOW_CANCEL = 2;
    public static final int STATUS_FOLLOW_QUERY = 3;


    public static final String RANKING_ORDER_FANS = "fans";
    public static final String RANKING_ORDER_TIME = "time";
    public static final String RANKING_ORDER_GOLD = "gold";

    public static final String WALLET_TYPE_WEIXIN = "weixin";
    public static final String WALLET_TYPE_ALIPAY = "alipay";

    public static final int FOLLOW_SPACING_TIME = 1000;

    /**
     * 更新用户信息
     *
     * @return
     */
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<UserInfo>> updateUser(@Body FormBody formBody);

    /**
     * 获取用户的关注和粉丝列表
     *
     * @param formBody
     * @return
     */
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<SearchResult[]>> getFollowList(@Body FormBody formBody);


    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<FollowInfo>> followUser(@Field("api") String api, @Field("action") String action,
                                                @Field("user_id") String userId,
                                                @Field("auth_accesskey") String accessKey,
                                                @Field("auth_secretkey") String secretKey);

    /**
     * 获取用户最新的统计数据
     * @param api
     * @param userId
     * @param accessKey
     * @param secretKey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<UserInfo>> getUserCount(@Field("api") String api, @Field("user_id") String userId,
                                                @Field("auth_accesskey") String accessKey,
                                                @Field("auth_secretkey") String secretKey);

    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<UserInfo>> updateAuth(@Field("api") String api, @Field("key") String key,
                                              @Field("auth_accesskey") String accessKey,
                                              @Field("auth_secretkey") String secretKey);



    /**
     * 获取用户个人信息
     *
     * @param user_id
     * @param accessKey
     * @param secretKey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<UserInfo[]>> getUserInfo(@Field("api") String api, @Field("user_id") String user_id,
                                                 @Field("field") String field,
                                                 @Field("auth_accesskey") String accessKey,
                                                 @Field("auth_secretkey") String secretKey);

    /**
     * 获取第三方用户个人信息
     *
     * @param user_id
     * @param accessKey
     * @param secretKey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<UserInfo[]>> getUsIdInfo(@Field("api") String api, @Field("usid") String user_id,
                                                 @Field("field") String field,
                                                 @Field("auth_accesskey") String accessKey,
                                                 @Field("auth_secretkey") String secretKey);

    /**
     * 绑定提现账户
     *
     * @param account   绑定的账号
     * @param type      绑定的账号类型 weixin or alipay
     * @param accessKey
     * @param secretKey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<WalletStatus[]>> bindWallet(@Field("api") String api, @Field("account") String account,
                                                    @Field("type") String type,
                                                    @Field("auth_accesskey") String accessKey,
                                                    @Field("auth_secretkey") String secretKey);

    /**
     * 查询钱包绑定状态
     *
     * @param userId    当前登陆的用户UID
     * @param accessKey
     * @param secretKey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<WalletStatus[]>> walletStatus(@Field("api") String api, @Field("user_id") String userId,
                                                      @Field("auth_accesskey") String accessKey,
                                                      @Field("auth_secretkey") String secretKey);


    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<WalletStatus[]>> unbindWallet(@Field("api") String api, @Field("password") String password,
                                                      @Field("type") String type,
                                                      @Field("auth_accesskey") String accessKey,
                                                      @Field("auth_secretkey") String secretKey);


}
