package com.zhiyicx.thinksnsplus.modules.guide;


import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class GuidePresenterModule {

    private final GuideContract.View mView;

    public GuidePresenterModule(GuideContract.View view) {
        mView = view;
    }

    @Provides
    GuideContract.View provideGuideContractView() {
        return mView;
    }
}
