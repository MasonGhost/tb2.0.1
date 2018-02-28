package com.zhiyicx.thinksnsplus.modules.invite.eidtcode;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class EditInviteCodePresenterModule {
    private final EditInviteCodeContract.View mView;

    public EditInviteCodePresenterModule(EditInviteCodeContract.View view) {
        mView = view;
    }

    @Provides
    EditInviteCodeContract.View provideContractView() {
        return mView;
    }

}
