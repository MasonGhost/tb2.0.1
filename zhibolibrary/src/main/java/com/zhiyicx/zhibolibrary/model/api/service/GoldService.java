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
