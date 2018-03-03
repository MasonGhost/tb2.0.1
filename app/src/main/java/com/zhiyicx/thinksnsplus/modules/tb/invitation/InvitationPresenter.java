package com.zhiyicx.thinksnsplus.modules.tb.invitation;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBShareLinkBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository.RETRY_DELAY_TIME;
import static com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository.MAX_RETRY_COUNTS;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InvitationPresenter extends AppBasePresenter<InvitationContract.View> implements InvitationContract.Presenter {
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public InvitationPresenter(InvitationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getShareLink() {
        Subscription subscribe = mUserInfoRepository.getShareLink()
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribe(new BaseSubscribeForV2<TBShareLinkBean>() {
                    @Override
                    protected void onSuccess(TBShareLinkBean data) {
                        mRootView.getShareLinkSuccess(data);
                    }
                });
        addSubscrebe(subscribe);

    }

    @Override
    public void shareTask() {
        Subscription subscribe = mUserInfoRepository.shareCount(null, null)
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                    }
                });
        addSubscrebe(subscribe);
    }
}
