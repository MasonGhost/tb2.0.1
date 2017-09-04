package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author Catherine
 * @describe 问题悬赏界面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QARewardPresenter extends AppBasePresenter<QARewardContract.RepositoryPublish, QARewardContract.View>
        implements QARewardContract.Presenter {

    @Inject
    CommentRepository mCommentRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    public QARewardPresenter(QARewardContract.RepositoryPublish repository, QARewardContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void publishQuestion(QAPublishBean qaPublishBean) {
        mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(new Func1<UserInfoBean, Observable<BaseJsonV2<QAPublishBean>>>() {
                    @Override
                    public Observable<BaseJsonV2<QAPublishBean>> call(UserInfoBean userInfoBean) {
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        if (userInfoBean.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                            if (userInfoBean.getWallet().getBalance() < qaPublishBean.getAmount()) {
                                mRootView.goRecharge(WalletActivity.class);
                                return Observable.error(new RuntimeException(""));
                            }
                        }
                        return mRepository.publishQuestion(qaPublishBean);
                    }
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    return null;
                }, () -> null)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<QAPublishBean>>() {
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
        Subscription subscription = mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(new Func1<UserInfoBean, Observable<BaseJsonV2<Object>>>() {
                    @Override
                    public Observable<BaseJsonV2<Object>> call(UserInfoBean userInfoBean) {
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        if (userInfoBean.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                            if (userInfoBean.getWallet().getBalance() < amount) {
                                mRootView.goRecharge(WalletActivity.class);
                                return Observable.error(new RuntimeException(""));
                            }
                        }
                        return mRepository.resetReward(question_id, amount);
                    }
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    return null;
                }, () -> null)
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
