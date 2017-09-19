package com.zhiyicx.zhibolibrary.model.api.service;

import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.TradeOrder;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBApiToken;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jess on 16/4/25.
 */
public interface GoldService {
    public static final int EXCHANGE_TYPE_ZAN = 1;//兑换赞
    public static final int EXCHANGE_TYPE_GOLD = 2;
    public static final int EXCHANGE_TYPE_WITHDRAW = 3;
    public static final int EXCHANGE_TYPE_TOP_UP = 4;
    public static final int EXCHANGE_TYPE_GIFT = 5;
    public static final int EXCHANGE_TYPE_ZAN_PRESENTER = 6;//赠送赞


    public static final String HISTORY_TYPE_ZAN = "zan";
    public static final String HISTORY_TYPE_GOLD = "gold";
//
//    /**
//     * 兑换
//     *
//     * @param token     通讯口令,每个口令有效期60s (当前时间+兑换消耗的类型+数量组成的md5字符串 )
//     * @param hextime   十六进制的时间戳
//     * @param count     消耗的赞的数量
//     * @param type      兑换消耗的产品类型 (此处为zan)
//     * @param accessKey 授权accesskey
//     * @param secretKey 授权secretkey
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/api/gold/exchange")
//    Observable<BaseJson<TradeOrder>> exchange(@Field("token") String token,
//                                              @Field("hextime") String hextime,
//                                              @Field("count") int count,
//                                              @Field("type") String type,
//                                              @Field("auth_accesskey") String accessKey,
//                                              @Field("auth_secretkey") String secretKey);

    /**
     * 生成口令
     *
     * @param token     通讯口令,每个口令有效期60s (当前时间戳+口令类型(type)+当前操作的用户uid组成的md5字符串)
     * @param hextime   十六进制的时间戳
     * @param userId    当前登陆的用户UID
     * @param type      兑换消耗的产品类型 (默认为1)
     * @param toUserId  涉及接收方的时候传递该参数
     * @param accessKey 授权accesskey
     * @param secretKey 授权secretkey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<ZBApiToken>> getPreToken(@Field("api") String api, @Field("token") String token,
                                                 @Field("hextime") String hextime,
                                                 @Field("user_id") String userId,
                                                 @Field("type") int type,
                                                 @Field("to_user_id") String toUserId,
                                                 @Field("auth_accesskey") String accessKey,
                                                 @Field("auth_secretkey") String secretKey);


    /**
     * @param preToken  预操作的口令
     * @param count     要兑换的数量
     * @param giftCode 赠送礼物的代号
     * @param accessKey 授权accesskey
     * @param secretKey 授权secretkey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<TradeOrder>> createOrder(@Field("api") String api, @Field("pre_token") String preToken,
                                                 @Field("count") int count,
                                                 @Field("gift_code") String giftCode,
                                                 @Field("params") String params,
                                                 @Field("auth_accesskey") String accessKey,
                                                 @Field("auth_secretkey") String secretKey);


    /**
     * 查看订单状态
     *
     * @param tradeOrder 订单号
     * @param accessKey  授权accesskey
     * @param secretKey  授权secretkey
     * @return
     */
    @FormUrlEncoded
    @POST(ZBLApi.EXTRAL_URL)
    Observable<BaseJson<UserInfo>> getOrderStatus(@Field("api") String api, @Field("trade_order") String tradeOrder,
                                                  @Field("auth_accesskey") String accessKey,
                                                  @Field("auth_secretkey") String secretKey);



}
