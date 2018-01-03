package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MembersPresenterModule {
    MembersContract.View mView;

    public MembersPresenterModule(MembersContract.View view) {
        mView = view;
    }

    @Provides
    MembersContract.View provideMembersContractView() {
        return mView;
    }

}
