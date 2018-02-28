package com.zhiyicx.thinksnsplus.modules.invite.eidtcode

import com.zhiyicx.baseproject.base.TSActivity
import com.zhiyicx.thinksnsplus.base.AppApplication

/**
 * @Describe 填写邀请码
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */

class EditInviteCodeActivity : TSActivity<EditInviteCodePresenter, EditInviteCodeFragment>() {

    override fun componentInject() {
        DaggerEditInviteCodeComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//                .settingsPresenterModule(EditInviteCodePresenterModule(mContanierFragment))
                .build()
                .inject(this)

    }

    override fun getFragment(): EditInviteCodeFragment {
        return EditInviteCodeFragment.newInstance()
    }


}

