package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTollPresenter extends AppBasePresenter<DynamicCommentTollContract.Repository, DynamicCommentTollContract.View>
        implements DynamicCommentTollContract.Presenter {

    @Inject
    public DynamicCommentTollPresenter(DynamicCommentTollContract.Repository repository, DynamicCommentTollContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void tollDynamicComment(Long feed_id) {
        if (mRootView.getCommentMoney() != (int) mRootView.getCommentMoney()) {
            mRootView.initWithdrawalsInstructionsPop();
            return;
        }
        mRepository.tollDynamicComment(feed_id)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing));
                    }
                })
                .subscribe(new BaseSubscribeForV2<BaseJson<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJson<Integer> data) {
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
    }
}
