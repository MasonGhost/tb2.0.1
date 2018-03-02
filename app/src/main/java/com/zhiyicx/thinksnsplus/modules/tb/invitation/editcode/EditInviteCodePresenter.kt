package com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode

import com.zhiyicx.common.mvp.BasePresenter
import com.zhiyicx.common.utils.ToastUtils

import javax.inject.Inject

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

class EditInviteCodePresenter @Inject
constructor(rootView: EditInviteCodeContract.View) : BasePresenter<EditInviteCodeContract.View>(rootView), EditInviteCodeContract.Presenter {


    override fun submitInviteCode(inviteCode: String) {
        mRootView.submitCallBack(false)
    }
}
