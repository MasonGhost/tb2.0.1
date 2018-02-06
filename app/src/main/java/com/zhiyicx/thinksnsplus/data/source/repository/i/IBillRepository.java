package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
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
import retrofit2.http.POST;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_BALANCE_TO_INTEGRATION;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/26
 * @Contact master.jungle68@gmail.com
 */
public interface IBillRepository {


    Observable<List<RechargeSuccessBean>> getBillList(int after, String action);

    Observable<List<RechargeSuccessBean>> dealRechargeList(Observable<List<RechargeSuccessBean>> data);

    Observable<WalletConfigBean> getWalletConfig();

    void getWalletConfigWhenStart(Long user_id);

    Observable<RechargeSuccessBean> rechargeSuccessCallBack(String charge);

    Observable<RechargeSuccessBean> rechargeSuccess(String charge);

    Observable<WithdrawResultBean> withdraw(double value, String type, String account);

    /**
     * @param after 获取更多数据，上一次获取列表的最后一条 ID
     * @return
     */
    Observable<List<WithdrawalsListBean>> getWithdrawListDetail(int after);

    /**
     * 获取支付信息
     *
     * @param channel 支付渠道
     * @param amount  支付金额
     * @return
     */
    Observable<PayStrV2Bean> getPayStr(String channel, double amount);

    /**
     * 钱包余额转积分
     *
     * @param amount 转账金额，分单位
     * @return
     */
    Observable<BaseJsonV2> balance2Integration(long amount);



    /*******************************************  积分  *********************************************/
    /**
     * @return 积分配置信息
     */
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
    Observable<PayStrV2Bean> getIntegrationPayStr(String type, long amount, String extra);


    /**
     * @param order
     * @return 取回凭据
     */
    Observable<RechargeSuccessV2Bean> integrationRechargeSuccess(String order);

    /**
     * @param limit  数据返回条数
     * @param after  翻页数据id
     * @param action 筛选类型 recharge - 充值记录 cash - 提现记录 默认为全部
     * @param type   筛选类型 1 - 收入 -1 - 支出 默认为全部
     * @return 积分流水
     */
    Observable<List<RechargeSuccessV2Bean>> integrationOrdersSuccess(int limit, int after, String
            action, Integer type);


    /**
     * 发起积分提现
     *
     * @param amount 提取积分，发起该操作后会根据积分兑换比例取人民币分单位整数后扣减相应积分
     * @return
     */
    Observable<BaseJsonV2> integrationWithdrawals(Integer amount);

}
