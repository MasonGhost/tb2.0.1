package com.zhiyicx.thinksnsplus.modules.information.dig;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */
@Module
public class InfoDigListPresenterModule {

    private InfoDigListContract.View mView;

    public InfoDigListPresenterModule(InfoDigListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public InfoDigListContract.View provideInfoDigListContractView(){
        return mView;
    }


}
