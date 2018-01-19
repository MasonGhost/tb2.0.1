package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/26
 * @Contact master.jungle68@gmail.com
 */
public interface IBillRepository {


    Observable<List<RechargeSuccessBean>> getBillList(int after,Integer action);
    Observable<List<RechargeSuccessBean>> dealRechargeList(Observable<List<RechargeSuccessBean>> data);

    Observable<WalletConfigBean> getWalletConfig();
    void getWalletConfigWhenStart(Long user_id);

    Observable<RechargeSuccessBean> rechargeSuccessCallBack(String charge);
    Observable<RechargeSuccessBean> rechargeSuccess(String charge);

    Observable<WithdrawResultBean> withdraw(double value, String type, String account);
    /**
     *
     * @param after 获取更多数据，上一次获取列表的最后一条 ID
     * @return
     */
    Observable<List<WithdrawalsListBean>> getWithdrawListDetail(int after);
}
