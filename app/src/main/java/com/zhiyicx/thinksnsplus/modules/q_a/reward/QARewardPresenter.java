package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QARewardPresenter extends AppBasePresenter<QARewardContract.RepositoryPublish, QARewardContract.View>
        implements QARewardContract.Presenter {

    @Inject
    public QARewardPresenter(QARewardContract.RepositoryPublish repository, QARewardContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void publishQuestion(QAPublishBean qaPublishBean) {
        mRepository.publishQuestion(qaPublishBean).subscribe(new BaseSubscribeForV2<BaseJsonV2<QAPublishBean>>() {
            @Override
            protected void onSuccess(BaseJsonV2<QAPublishBean> data) {
                mRootView.showSnackSuccessMessage(data.getMessage().get(0));
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

    @Override
    public void resetReward(Long question_id, double amount) {
        mRootView.showSnackLoadingMessage(mContext.getString(R.string.bill_doing));
        Subscription subscription = mRepository.resetReward(question_id, amount)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.qa_reset_reward_success));
                        mRootView.resetRewardSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mRepository.getDraftQuestion(qestion_mark);
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        mRepository.saveQuestion(qestion);
    }
}
