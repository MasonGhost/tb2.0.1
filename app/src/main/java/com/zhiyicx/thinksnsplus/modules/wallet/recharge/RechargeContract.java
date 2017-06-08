package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.tspay.TSPayClient;

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
    }

    interface Repository {


    }

    interface Presenter extends IBaseTouristPresenter {
        void getPayStr(@TSPayClient.PayKey String channel, int amount);
    }
}
