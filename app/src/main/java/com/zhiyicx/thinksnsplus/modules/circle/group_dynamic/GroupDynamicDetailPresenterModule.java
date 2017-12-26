package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */
@Module
public class GroupDynamicDetailPresenterModule {

    private GroupDynamicDetailContract.View mView;

    public GroupDynamicDetailPresenterModule(GroupDynamicDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public GroupDynamicDetailContract.View provideGroupDynamicDetailContractView(){
        return mView;
    }


}
