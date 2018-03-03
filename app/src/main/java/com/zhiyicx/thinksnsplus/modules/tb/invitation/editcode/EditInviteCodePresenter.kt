package com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode

import com.zhiyicx.common.base.BaseJsonV2
import com.zhiyicx.common.mvp.BasePresenter
import com.zhiyicx.common.utils.ToastUtils
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository
import org.json.JSONObject
import rx.functions.Action1

import javax.inject.Inject

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

class EditInviteCodePresenter @Inject
constructor(rootView: EditInviteCodeContract.View) : BasePresenter<EditInviteCodeContract.View>(rootView), EditInviteCodeContract.Presenter {
    @Inject
    lateinit var mUserInfoRepository: UserInfoRepository


    override fun submitInviteCode(inviteCode: String) {
        val code: Int
        try {
            code = inviteCode.toInt()
            addSubscrebe(mUserInfoRepository.submitInviteCode(code)
                    .subscribe(object : BaseSubscribeForV2<BaseJsonV2<*>>() {
                        override fun onSuccess(data: BaseJsonV2<*>) {
                            val msg: String = JSONObject(data.toString()).optString("message")
                            if (msg == "绑定成功") mRootView.submitCallBack(true, msg) else mRootView.submitCallBack(false, msg)
                        }

                        override fun onFailure(message: String?, code: Int) {
                            super.onFailure(message, code)
                            if (message == "绑定成功") mRootView.submitCallBack(true, message) else mRootView.submitCallBack(false, message!!)
                        }
                    }))
        } catch (e: NumberFormatException) {
            ToastUtils.showLongToast("邀请码格式错误")
        }
    }
}
