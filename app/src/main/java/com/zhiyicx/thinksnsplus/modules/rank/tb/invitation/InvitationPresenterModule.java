package com.zhiyicx.thinksnsplus.modules.rank.tb.invitation;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class InvitationPresenterModule {
    InvitationContract.View mView;

    public InvitationPresenterModule(InvitationContract.View view) {
        mView = view;
    }

    @Provides
    InvitationContract.View providesInvitationView(){
        return mView;
    }
}
