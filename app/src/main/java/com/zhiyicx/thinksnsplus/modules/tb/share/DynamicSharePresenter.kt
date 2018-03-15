package com.zhiyicx.thinksnsplus.modules.tb.share

import com.zhiyicx.common.base.BaseJsonV2
import com.zhiyicx.common.utils.ToastUtils
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay
import com.zhiyicx.thinksnsplus.R
import com.zhiyicx.thinksnsplus.base.AppBasePresenter
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBShareLinkBean
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.MAX_RETRY_COUNTS
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.RETRY_DELAY_TIME
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

class DynamicSharePresenter @Inject
constructor(rootView: DynamicShareContract.View) : AppBasePresenter<DynamicShareContract.View>(rootView), DynamicShareContract.Presenter {


    @Inject
    lateinit var mUserInfoRepository: UserInfoRepository

    override fun shareTask(id: String) {
        mUserInfoRepository.shareCount(UserInfoRepository.SHARETYPEENUM.FEED.value, id)
                .subscribe(object : BaseSubscribeForV2<BaseJsonV2<*>?>() {
                    override fun onSuccess(data: BaseJsonV2<*>?) {
                    }
                })
    }

    override fun getShareLink() {
        val subscribe = mUserInfoRepository.getShareLink()
                .retryWhen(RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribe(object : BaseSubscribeForV2<TBShareLinkBean>() {
                    override fun onSuccess(data: TBShareLinkBean) {
                        mRootView.getShareLinkSuccess(data)
                    }
                })
        addSubscrebe(subscribe)
    }

    /**
     *
     */
    override fun submitInviteCode(inviteCode: String) {
        ToastUtils.showLongToast(inviteCode)
    }
}
