package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseRewardRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RewardPresenter extends AppBasePresenter< RewardContract.View> implements RewardContract.Presenter {


    @Inject
    BaseRewardRepository mRewardRepository;

    @Inject
    public RewardPresenter( RewardContract.View rootView) {
        super( rootView);
    }

    @Override
    public void reward(double rewardMoney, RewardType rewardType, long sourceId) {

        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataByUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
        switch (rewardType) {
            case INFO: // 咨询打赏
                hanldeRewardResult(mRewardRepository.rewardInfo(sourceId, rewardMoney), walletBean, rewardMoney);
                break;
            case DYNAMIC: // 动态打赏
                hanldeRewardResult(mRewardRepository.rewardDynamic(sourceId, rewardMoney), walletBean, rewardMoney);
                break;
            case USER: // 用户打赏
                hanldeRewardResult(mRewardRepository.rewardUser(sourceId, rewardMoney), walletBean, rewardMoney);
                break;
            case QA_ANSWER: // 问答回答打赏
                hanldeRewardResult(mRewardRepository.rewardQA(sourceId, rewardMoney), walletBean, rewardMoney);
                break;

            case POST: // 帖子打赏
                hanldeRewardResult(mRewardRepository.rewardPost(sourceId, rewardMoney), walletBean, rewardMoney);
                break;

            default:
                mRootView.showSnackErrorMessage(mContext.getString(R.string.reward_type_error));

        }
    }

    private void hanldeRewardResult(Observable<Object> result, WalletBean walletBean, double rewardMoney) {
        Subscription subscription = handleWalletBlance((long) rewardMoney)
//                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
//                        .string.transaction_doing)))
                .flatMap(o -> result).doAfterTerminate(() -> mRootView.setSureBtEnable(true))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        walletBean.setBalance(walletBean.getBalance() - rewardMoney);
                        mWalletBeanGreenDao.insertOrReplace(walletBean);
                        mRootView.rewardSuccess(rewardMoney);
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
