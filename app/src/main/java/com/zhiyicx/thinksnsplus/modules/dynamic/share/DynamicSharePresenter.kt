package com.zhiyicx.thinksnsplus.modules.dynamic.share

import com.zhiyicx.common.utils.ToastUtils
import com.zhiyicx.thinksnsplus.base.AppBasePresenter
import javax.inject.Inject

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

class DynamicSharePresenter @Inject
constructor(rootView: DynamicShareContract.View) : AppBasePresenter<DynamicShareContract.View>(rootView), DynamicShareContract.Presenter {


    override fun submitInviteCode(inviteCode: String) {
        ToastUtils.showLongToast(inviteCode)
    }
}
