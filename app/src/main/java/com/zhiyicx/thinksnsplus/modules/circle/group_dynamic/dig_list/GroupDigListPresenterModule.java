package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */
@Module
public class GroupDigListPresenterModule {

    private GroupDigListContract.View mView;

    public GroupDigListPresenterModule(GroupDigListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public GroupDigListContract.View provideGroupDigListContractView(){
        return mView;
    }

}
