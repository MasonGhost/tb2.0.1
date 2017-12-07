package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;

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

            case POST: // 帖子打赏
                hanldeRewardResult(mRepository.rewardPost(sourceId, rewardMoney), walletBean, rewardMoney);
                break;

            default:
                mRootView.showSnackErrorMessage(mContext.getString(R.string.reward_type_error));

        }
    }

    private void hanldeRewardResult(Observable<Object> result, WalletBean walletBean, double rewardMoney) {
        Subscription subscription = handleWalletBlance((long) rewardMoney)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(o -> result).doAfterTerminate(() -> mRootView.setSureBtEnable(true))
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
                        if (isBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.reward_failed));
                    }
                });
        addSubscrebe(subscription);
    }
}
