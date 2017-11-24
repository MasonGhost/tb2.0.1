package com.zhiyicx.zhibolibrary.model.impl;


import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.StarExchangeModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.CommonService;
import com.zhiyicx.zhibolibrary.model.api.service.GoldService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.Config;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.TradeOrder;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBApiToken;

import okhttp3.FormBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/26.
 */
public class StarExchangeModelImpl implements StarExchangeModel {
    private GoldService mService;
    private CommonService mCommonService;

    public StarExchangeModelImpl(ServiceManager manager) {
        this.mService = manager.getGoldService();
        this.mCommonService=manager.getCommonService();
    }

//    @Override
//    public Observable<BaseJson<TradeOrder>> exchange(String token, String hextime
//            , int count, String type, String accessKey, String secretKey) {
//        return mService.exchange(token, hextime, count, type, accessKey, secretKey);
//    }

    /**
     *      * @param token     通讯口令,每个口令有效期60s (当前时间戳+口令类型(type)+当前操作的用户uid组成的md5字符串)
     * @param hextime   十六进制的时间戳
     * @param userId    当前登陆的用户UID
     * @param type      兑换消耗的产品类型 (默认为1)
     * @param toUserId  涉及接收方的时候传递该参数

     * @return
     */
    @Override
    public Observable<BaseJson<ZBApiToken>> getPreToken(String token, String hextime, String userId
            , int type, String toUserId) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_GET_TRADE_PRETOKEN);
        builder.add("user_id", userId);
        if(toUserId!=null)
        builder.add("to_user_id", toUserId);
        builder.add("type", type+"");
        builder.add("token", token);
        builder.add("hextime", hextime);
//        PermissionData[] permissionDatas = ZhiboApplication.getPermissionDatas();
//        for (PermissionData data : permissionDatas) {
//            builder.add(data.auth_key, data.auth_value);
//        }
        FormBody formBody = builder.build();
        return mService.getPreToken(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<BaseJson<TradeOrder>> createOrder(String preToken, int count, String giftCode
            , String params) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_CREATE_TRADE);
        builder.add("pre_token", preToken);
        builder.add("count", count+"");
        if(giftCode!=null)
        builder.add("gift_code", giftCode);
        if(params!=null)
        builder.add("params",params);
//        PermissionData[] permissionDatas = ZhiboApplication.getPermissionDatas();
//        for (PermissionData data : permissionDatas) {
//            builder.add(data.auth_key, data.auth_value);
//        }
        FormBody formBody = builder.build();
        return mService.createOrder(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());
    }


    @Override
    public Observable<BaseJson<UserInfo>> getOrderStatus(String tradeOrder) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("api", ZBLApi.API_GET_TRADE_STATUS);
        builder.add("trade_order", tradeOrder);
//        PermissionData[] permissionDatas = ZhiboApplication.getPermissionDatas();
//        for (PermissionData data : permissionDatas) {
//            builder.add(data.auth_key, data.auth_value);
//        }
        FormBody formBody = builder.build();
        return mService.getOrderStatus(ZBLApi.CONFIG_BASE_DOMAIN, formBody).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BaseJson<Config>> getConfig(String hextime,
                                                  String token,
                                                  String name) {
        return mCommonService.getConfig(ZBLApi.API_GET_CONFIG,hextime, token, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将配置文件到内存中
     */
    @Override
    public void setConfig(Config config) {

        if (config.sns_timespan != null) {
            ZBLApi.SNS_TIME = config.sns_timespan;
        }
        if (config.exchange_type_list != null) {
            ZBLApi.EXCHANGE_TYPE_LIST = config.exchange_type_list;
        }

    }
}
