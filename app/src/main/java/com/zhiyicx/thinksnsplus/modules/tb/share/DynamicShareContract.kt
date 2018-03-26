package com.zhiyicx.thinksnsplus.modules.tb.share

import com.zhiyicx.baseproject.base.IBaseTouristPresenter
import com.zhiyicx.common.mvp.i.IBasePresenter
import com.zhiyicx.common.mvp.i.IBaseView
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBShareLinkBean

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/28
 * @Contact master.jungle68@gmail.com
 */

interface DynamicShareContract {

    interface View : IBaseView<Presenter> {
        fun getShareLinkSuccess(data: TBShareLinkBean)
    }

    interface Presenter : IBaseTouristPresenter {
        /**
         * 提交邀请码
         *
         * @param inviteCode 邀请码
         */
        fun submitInviteCode(inviteCode: String)

        fun shareTask(sourceId:DynamicShareBean)
        fun getShareLink()
    }

}
