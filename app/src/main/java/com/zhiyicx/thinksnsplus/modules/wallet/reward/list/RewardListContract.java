package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardContract;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public interface RewardListContract {
    interface View extends ITSListView<RewardsListBean, Presenter> {
        /**
         *
         * @return  current reward type
         */
        RewardType getCurrentType();

        /**
         *
         * @return current reward source's id
         */
        long getSourceId();

        /**
         * 获取打赏缓存数据
         * @return
         */
        List<RewardsListBean> getCacheData();
    }

    interface Presenter extends ITSListPresenter<RewardsListBean> {


    }

}
