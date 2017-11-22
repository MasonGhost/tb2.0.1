package com.zhiyicx.zhibolibrary.model.api.service;

import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;
import com.zhiyicx.zhibolibrary.model.entity.TradeOrder;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBApiToken;

import okhttp3.FormBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
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

     * @return
     */
    @POST
    Observable<BaseJson<ZBApiToken>> getPreToken(@Url String url, @Body FormBody formBody);


    /**

     * @return
     */
    @POST
    Observable<BaseJson<TradeOrder>> createOrder(@Url String url, @Body FormBody formBody);


    /**
     * 查看订单状态
     *

     * @return
     */
    @POST
    Observable<BaseJson<UserInfo>> getOrderStatus(@Url String url, @Body FormBody formBody);

    /**
     * 获取金币兑换的历史记录
     *
     * @param formBody
     * @return
     */
    @POST
    Observable<BaseJson<GoldHistoryJson[]>> getGoldList(@Url String url,@Body FormBody formBody);

}
