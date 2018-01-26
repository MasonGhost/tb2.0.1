package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.tspay.TSPayClient;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface RechargeContract {

    interface View extends IBaseView<Presenter> {
        void payCredentialsResult(PayStrBean payStrBean);
        void configSureBtn(boolean enable);
        void rechargeSuccess(RechargeSuccessBean rechargeSuccessBean);
        void initmRechargeInstructionsPop();
        double getMoney();

        boolean useInputMonye();
    }

    interface Presenter extends IBaseTouristPresenter {
        void getPayStr(@TSPayClient.PayKey String channel, double amount);
        void rechargeSuccess(String charge);
        void rechargeSuccessCallBack(String charge);
    }
}
