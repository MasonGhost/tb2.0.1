package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@Module
public class QARewardPresenterModule {

    private QARewardContract.View mView;

    public QARewardPresenterModule(QARewardContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public QARewardContract.View provideQA$RewardContractView(){
        return mView;
    }

}
