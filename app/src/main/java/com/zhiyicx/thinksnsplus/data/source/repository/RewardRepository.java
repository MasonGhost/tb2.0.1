package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardContract;

import javax.inject.Inject;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */
public class RewardRepository extends BaseRewardRepository implements RewardContract.Repository {


    @Inject
    public RewardRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

}
