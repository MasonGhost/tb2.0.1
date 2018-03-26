package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public interface RewardContract {

    interface View extends IBaseView<Presenter> {


        void rewardSuccess(double rewardMoney);

        void setSureBtEnable(boolean b);
    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * reward
         *
         * @param rewardMoney want to reward money
         * @param rewardType  type for reward {@link RewardType}
         * @param sourceId    this reward  target source id
         */
        void reward(double rewardMoney, RewardType rewardType, long sourceId);
    }
}
