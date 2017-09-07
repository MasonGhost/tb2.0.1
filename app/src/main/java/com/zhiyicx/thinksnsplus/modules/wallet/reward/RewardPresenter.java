package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RewardPresenter extends AppBasePresenter<RewardContract.Repository, RewardContract.View> implements RewardContract.Presenter {

    public static final int DEFAULT_DELAY_TIME = 2;

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    CommentRepository mCommentRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public RewardPresenter(RewardContract.Repository repository, RewardContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void reward(double rewardMoney, RewardType rewardType, long sourceId) {

        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataByUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
        double balance = 0;
        if (walletBean != null) {
            balance = walletBean.getBalance();
        }

        if (balance < rewardMoney) {
            mRootView.goRecharge(WalletActivity.class);
            return;
        }
        switch (rewardType) {
            case INFO: // 咨询打赏
                hanldeRewardResult(mRepository.rewardInfo(sourceId, rewardMoney), walletBean, rewardMoney);
                break;
            case DYNAMIC: // 动态打赏
                hanldeRewardResult(mRepository.rewardDynamic(sourceId, rewardMoney), walletBean, rewardMoney);
                break;
            case USER: // 用户打赏
                hanldeRewardResult(mRepository.rewardUser(sourceId, rewardMoney), walletBean, rewardMoney);
                break;
            case QA_ANSWER: // 问答回答打赏
                hanldeRewardResult(mRepository.rewardQA(sourceId, rewardMoney), walletBean, rewardMoney);
                break;

            default:
                mRootView.showSnackErrorMessage(mContext.getString(R.string.reward_type_error));

        }
    }

    private void hanldeRewardResult(Observable<Object> result, WalletBean walletBean, double rewardMoney) {

        Subscription subscription = mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.apply_doing)))
                .flatMap(new Func1<UserInfoBean, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(UserInfoBean userInfoBean) {
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        if (userInfoBean.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                            if (userInfoBean.getWallet().getBalance() < rewardMoney) {
                                mRootView.goRecharge(WalletActivity.class);
                                return Observable.error(new RuntimeException(""));
                            }
                        }
                        return result;
                    }
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    return null;
                }, () -> null)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        walletBean.setBalance(walletBean.getBalance() - rewardMoney);
                        mWalletBeanGreenDao.insertOrReplace(walletBean);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.reward_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscription);
    }
}
