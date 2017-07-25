package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.QA$RewardContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class QA$RewardRepository extends BaseQuestionRepository implements QA$RewardContract.Repository{

    @Inject
    public QA$RewardRepository(ServiceManager manager) {
        super(manager);
    }
}
