package com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@Module
public class ChooseCirclePresenterModule {

    private ChooseCircleContract.View mView;

    public ChooseCirclePresenterModule(ChooseCircleContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public ChooseCircleContract.View provideChooseCircleContractView(){
        return mView;
    }

}
