package com.zhiyicx.thinksnsplus.modules.feedback;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/06/02/17:23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class FeedBackPresenter extends AppBasePresenter<FeedBackContract.View>
        implements FeedBackContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public FeedBackPresenter(FeedBackContract.View rootView) {
        super(rootView);
    }

    @Override
    public void submitFeedBack(String content, String contract) {
//        if (!(RegexUtils.isMobileExact(contract) && !RegexUtils.isEmail(content))) {
//            mRootView.showWithdrawalsInstructionsPop();
//            return;
//        }

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        Subscription subscribe = mSystemRepository.systemFeedback(content, Long.parseLong(comment_mark))
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.feed_back_ing)))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.feed_back_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.feed_back_failed));
                    }
                });
        addSubscrebe(subscribe);

    }
}
