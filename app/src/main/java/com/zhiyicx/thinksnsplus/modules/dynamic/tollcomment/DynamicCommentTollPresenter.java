package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTollPresenter extends AppBasePresenter<DynamicCommentTollContract.View>
        implements DynamicCommentTollContract.Presenter {

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Inject
    public DynamicCommentTollPresenter( DynamicCommentTollContract.View rootView) {
        super( rootView);
    }

    @Override
    public void setDynamicCommentToll(Long feed_id, int amout) {
        Subscription subscribe = mBaseDynamicRepository.setDynamicCommentToll(feed_id, amout)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing)))
                .subscribe(new BaseSubscribeForV2<DynamicCommentToll>() {
                    @Override
                    protected void onSuccess(DynamicCommentToll data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_comment_toll_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscribe);
    }

}
