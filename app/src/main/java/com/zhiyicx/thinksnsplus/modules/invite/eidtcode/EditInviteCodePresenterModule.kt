package com.zhiyicx.thinksnsplus.modules.invite.eidtcode

import dagger.Module
import dagger.Provides

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
class EditInviteCodePresenterModule(private val mView: EditInviteCodeContract.View) {

    @Provides
    internal fun provideContractView(): EditInviteCodeContract.View {
        return mView
    }

}
