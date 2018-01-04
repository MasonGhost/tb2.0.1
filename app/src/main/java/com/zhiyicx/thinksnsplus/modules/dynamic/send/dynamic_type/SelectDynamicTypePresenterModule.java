package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */
@Module
public class SelectDynamicTypePresenterModule {

    private SelectDynamicTypeContract.View mView;

    public SelectDynamicTypePresenterModule(SelectDynamicTypeContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SelectDynamicTypeContract.View provideSelectDynamicTypeContractView(){
        return mView;
    }

}
