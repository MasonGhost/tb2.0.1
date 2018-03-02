package com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode

import com.zhiyicx.common.mvp.i.IBasePresenter
import com.zhiyicx.common.mvp.i.IBaseView

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/28
 * @Contact master.jungle68@gmail.com
 */

interface EditInviteCodeContract {

    interface View : IBaseView<Presenter> {
        fun submitCallBack(b: Boolean)
    }

    interface Presenter : IBasePresenter {
        /**
         * 提交邀请码
         *
         * @param inviteCode 邀请码
         */
        fun submitInviteCode(inviteCode: String)
    }

}
