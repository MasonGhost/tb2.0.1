package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */
@Module
public class TBMarkDetailPresenterModule {
    TBMarkDetailContract.View mView;

    public TBMarkDetailPresenterModule(TBMarkDetailContract.View view) {
        mView = view;
    }

    @Provides
    TBMarkDetailContract.View provideTBMarkDetailContractView(){
        return mView;
    }
}
