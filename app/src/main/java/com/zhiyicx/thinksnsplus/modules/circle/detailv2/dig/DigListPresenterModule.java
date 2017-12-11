package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */
@Module
public class DigListPresenterModule {

    private DigListContract.View mView;

    public DigListPresenterModule(DigListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    DigListContract.View provideDigListContractView() {
        return mView;
    }

    @Provides
    DigListContract.Repository provideDigListContractRepository() {
        return new NotNull();
    }

    class NotNull implements DigListContract.Repository{

    }
}
