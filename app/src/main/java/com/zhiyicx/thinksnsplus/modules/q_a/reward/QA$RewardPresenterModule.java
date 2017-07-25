package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.thinksnsplus.data.source.repository.QA$RewardRepositoryPublish;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@Module
public class QA$RewardPresenterModule {

    private QA$RewardContract.View mView;

    public QA$RewardPresenterModule(QA$RewardContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public QA$RewardContract.View provideQA$RewardContractView(){
        return mView;
    }

    @Provides
    public QA$RewardContract.RepositoryPublish provideQA$RewardContractRepository(QA$RewardRepositoryPublish rewardRepository){
        return rewardRepository;
    }
}
