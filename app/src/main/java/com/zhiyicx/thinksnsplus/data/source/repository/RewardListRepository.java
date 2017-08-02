package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardContract;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.list.RewardListContract;

import javax.inject.Inject;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */
public class RewardListRepository extends BaseRewardRepository implements RewardListContract.Repository {


    @Inject
    public RewardListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

}
