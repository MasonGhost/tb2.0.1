package com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication
import com.zhiyicx.thinksnsplus.modules.invite.eidtcode.DaggerEditInviteCodeComponent

/**
 * @Describe 填写邀请码
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */

class EditInviteCodeActivity : TSActivity<EditInviteCodePresenter, EditInviteCodeFragment>() {

    override fun componentInject() {
        DaggerEditInviteCodeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editInviteCodePresenterModule(EditInviteCodePresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): EditInviteCodeFragment {
        return EditInviteCodeFragment.newInstance()
    }


}

