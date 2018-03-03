package com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode

import com.zhiyicx.common.base.BaseJsonV2
import com.zhiyicx.common.mvp.BasePresenter
import com.zhiyicx.common.utils.ToastUtils
import com.zhiyicx.thinksnsplus.R
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
        addSubscrebe(mUserInfoRepository.submitInviteCode(inviteCode)
                .subscribe(object : BaseSubscribeForV2<BaseJsonV2<*>>() {
                    override fun onSuccess(data: BaseJsonV2<*>) {
                        mRootView.submitCallBack(true, "")

                    }

                    override fun onFailure(message: String?, code: Int) {
                        super.onFailure(message, code)
                        mRootView.submitCallBack(false, message!!)
                    }

                    override fun onException(throwable: Throwable?) {
                        super.onException(throwable)
                        mRootView.submitCallBack(false, mContext.getString(R.string.err_net_not_work))

                    }
                }))

    }
}
