package com.zhiyicx.thinksnsplus.modules.dynamic.share

import com.zhiyicx.common.mvp.BasePresenter
import com.zhiyicx.common.utils.ToastUtils

import javax.inject.Inject

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

class DynamicSharePresenter @Inject
constructor(rootView: DynamicShareContract.View) : BasePresenter<DynamicShareContract.View>(rootView), DynamicShareContract.Presenter {


    override fun submitInviteCode(inviteCode: String) {
        ToastUtils.showLongToast(inviteCode)
    }
}
