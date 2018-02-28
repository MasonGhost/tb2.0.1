package com.zhiyicx.thinksnsplus.modules.rank.tb;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:45
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class RankListPresenterModule {
    RankListContract.View mView;

    public RankListPresenterModule(RankListContract.View view) {
        mView = view;
    }

    @Provides
    RankListContract.View provideRankListView(){
        return mView;
    }
}
