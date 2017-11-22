package com.zhiyicx.zhibolibrary.model;

import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.Config;
import com.zhiyicx.zhibolibrary.model.entity.TradeOrder;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBApiToken;

import rx.Observable;

/**
 * Created by jess on 16/4/26.
 */
public interface StarExchangeModel {



    Observable<BaseJson<ZBApiToken>> getPreToken(String token,
                                                 String hextime,
                                                 String userId,
                                                 int type,
                                                 String toUserId
                                                );

    Observable<BaseJson<TradeOrder>> createOrder(String preToken,
                                                 int count,
                                                 String giftCode,
                                                 String params);

    Observable<BaseJson<UserInfo>> getOrderStatus(String tradeOrder);

    Observable<BaseJson<Config>> getConfig(String hextime,
                                           String token,
                                           String name);
    void setConfig(Config config);
}
