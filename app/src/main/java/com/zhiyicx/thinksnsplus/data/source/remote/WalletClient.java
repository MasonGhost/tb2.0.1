package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_CONFIG;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_RECHARGE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_RECHARGE_SUCCESS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_RECHARGE_SUCCESS_CALLBACK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_CONFIG;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_WITHDRAW;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */
public interface WalletClient {

    @GET(APP_PAHT_WALLET_CONFIG)
    Observable<WalletConfigBean> getWalletConfig();

    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_WITHDRAW)
    Observable<WithdrawResultBean> withdraw(@Field("value") long value, @Field("type") String type, @Field("account") String account);

    @GET(APP_PAHT_WALLET_WITHDRAW)
    Observable<List<WithdrawalsListBean>> getWithdrawList(@Query("limit") int limit, @Query("after") int after);

    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS)
    Observable<RechargeSuccessBean> rechargeSuccess(@Path("charge") String charge);

    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST)
    Observable<List<RechargeSuccessBean>> getRechargeSuccessList(@Query("limit") int limit, @Query("after") int after, @Query("action") Integer
            action);

    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK)
    Observable<RechargeSuccessBean> rechargeSuccessCallBack(@Path("charge") String charge);

    /**
     * 获取支付信息
     */
    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_RECHARGE)
    Observable<PayStrBean> getPayStr(@Field("type") String channel, @Field("amount") long amount);

    /*******************************************  积分  *********************************************/
    /**
     * @return 积分配置信息
     */
    @GET(APP_PAHT_INTEGRATION_CONFIG)
    Observable<IntegrationConfigBean> getIntegrationConfig();

    /**
     * 获取积分充值信息
     */
    /**
     * @param type   充值方式 （见「启动信息接口」或者「钱包信息」）
     * @param amount 用户充值金额，单位为真实货币「分」单位，充值完成后会根据积分兑换比例增加相应数量的积分
     * @param extra  object,array 拓展信息字段，见 支付渠道-extra-参数说明
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PAHT_INTEGRATION_RECHARGE)
    Observable<PayStrV2Bean> getIntegrationPayStr(@Field("type") String type, @Field("amount") long amount, @Field("extra") String extra);


    /**
     * 取回凭据
     *
     * @param order
     * @return
     */
    @GET(APP_PAHT_INTEGRATION_RECHARGE_SUCCESS)
    Observable<RechargeSuccessV2Bean> integrationRechargeSuccess(@Path("order") String order);

}
