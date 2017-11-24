package com.zhiyicx.zhibolibrary.model.api.service;

import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.model.entity.WalletStatus;

import okhttp3.FormBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
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
     * 获取用户的关注和粉丝列表
     *
     * @param formBody
     * @return
     */
    @POST
    Observable<BaseJson<SearchResult[]>> getFollowList(@Url String url,@Body FormBody formBody);


    @POST
    Observable<BaseJson<FollowInfo>>  followUser(@Url String url, @Body FormBody formBody);


    /**
     * 获取第三方用户个人信息
     *
     * @param url
     * @param formBody
     * @return
     */
    @POST
    Observable<BaseJson<UserInfo[]>> getUsIdInfobyFrom(@Url String url, @Body FormBody formBody);

    /**
     * 通过票据获取权限信息
     *
     * @param url
     * @param api
     * @param ticket
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<BaseJson<PermissionData[]>> getPermission(@Url String url, @Field("api") String api,
                                                         @Field("ticket") String ticket);


}
