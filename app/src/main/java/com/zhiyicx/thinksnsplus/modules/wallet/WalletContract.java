package com.zhiyicx.thinksnsplus.modules.wallet;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface WalletContract {

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

    }

    interface Repository {


    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * update user info
         */
        void updateUserInfo();

        /**
         *
         * @return  true when first looking wallet page
         */
        boolean checkIsNeedTipPop();
    }
}
