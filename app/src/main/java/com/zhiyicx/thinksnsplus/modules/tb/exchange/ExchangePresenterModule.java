package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import dagger.Module;
import dagger.Provides;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */
@Module
public class ExchangePresenterModule {
    ExchangeContract.View mView;

    public ExchangePresenterModule(ExchangeContract.View view) {
        mView = view;
    }

    @Provides
    ExchangeContract.View provideExchangeContractView(){
        return mView;
    }
}
