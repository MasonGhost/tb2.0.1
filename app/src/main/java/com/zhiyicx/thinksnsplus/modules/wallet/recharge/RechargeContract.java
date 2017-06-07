package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface RechargeContract {

    interface View extends IBaseView<Presenter> {
        void payCredentialsResult(PayStrBean payStrBean);
    }

    interface Repository {


    }

    interface Presenter extends IBaseTouristPresenter {
        void getPayStr(String channel, int amount);
    }
}
