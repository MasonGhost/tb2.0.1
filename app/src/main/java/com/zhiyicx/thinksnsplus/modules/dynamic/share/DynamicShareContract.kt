package com.zhiyicx.thinksnsplus.modules.dynamic.share

import com.zhiyicx.common.mvp.i.IBasePresenter
import com.zhiyicx.common.mvp.i.IBaseView

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/28
 * @Contact master.jungle68@gmail.com
 */

interface DynamicShareContract {

    interface View : IBaseView<Presenter>

    interface Presenter : IBasePresenter {
        /**
         * 提交邀请码
         *
         * @param inviteCode 邀请码
         */
        fun submitInviteCode(inviteCode: String)
    }

}
