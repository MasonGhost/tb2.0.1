package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@Module
public class ExpertSearchPresenterModule {

    private ExpertSearchContract.View mView;

    public ExpertSearchPresenterModule(ExpertSearchContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public ExpertSearchContract.View provideExpertSearchContractView(){
        return mView;
    }

}
