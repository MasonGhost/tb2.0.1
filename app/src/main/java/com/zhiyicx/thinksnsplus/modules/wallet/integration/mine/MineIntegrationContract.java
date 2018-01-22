package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface MineIntegrationContract {

    interface View extends IBaseView<Presenter> {
        /**
         * update balance
         *
         * @param balance current user's balance
         */
        void updateBalance(double balance);

        /**
         * handle request loading
         *
         * @param isShow true ,show loading
         */
        void handleLoading(boolean isShow);

        /**
         * wallet callback
         *
         * @param configBean integration config info
         * @param tag              action tag, 1 recharge 2 withdraw
         */
        void integrationConfigCallBack(IntegrationConfigBean configBean, int tag);

    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * update user info
         */
        void updateUserInfo();

        /**
         * @return true when first looking wallet page
         */
        boolean checkIsNeedTipPop();

        String getTipPopRule();

        /**
         * check wallet config info, if walletconfig has cach used it or get it from server
         *
         * @param tag action tag
         */
        void checkIntegrationConfig(int tag, final boolean isNeedTip);
    }
}
