package com.zhiyicx.thinksnsplus.modules.settings.account;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public interface AccountManagementContract {

    interface View extends IBaseView<Presenter> {
        /**
         * update baind status
         *
         * @param data                bind accounts
         */
        void updateBindStatus(List<String> data);

        /**
         * bind success call back
         *
         * @param provider
         */
        void bindThirdSuccess(String provider);

        /**
         * unbind success call back
         *
         * @param provider
         */
        void unBindThirdSuccess(String provider);

        void updateUserinfo(UserInfoBean singleDataFromCache);
    }

    interface Presenter extends IBasePresenter {
        /**
         * get third binded account
         */
        void getBindSocialAcounts();

        /**
         * @param provider    type
         * @param accessToken accesse token
         * @param isBind      true to bind ,false to unbind
         */
        void bindOrUnbindThirdAccount(String provider, String accessToken, boolean isBind);

        void updaeUserInfo();
    }

}
