package com.zhiyicx.thinksnsplus.modules.rank.main.list;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */
@Module
public class RankListPresenterModule {

    private RankListContract.View mView;

    public RankListPresenterModule(RankListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public RankListContract.View provideRankListContractView(){
        return mView;
    }

}
