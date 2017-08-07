package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

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
            case INFO:
                hanldeRewardResult(mRepository.rewardInfo(sourceId, rewardMoney));
                break;
            case DYNAMIC:
                hanldeRewardResult(mRepository.rewardDynamic(sourceId, rewardMoney));
                break;

            default:

        }
    }

    private void hanldeRewardResult(Observable<Object> result) {
        Subscription subscription = result.subscribe(new BaseSubscribeForV2<Object>() {
            @Override
            protected void onSuccess(Object data) {
                mRootView.showSnackSuccessMessage(mContext.getString(R.string.reward_success));
                Observable.timer(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
                        .subscribe(aLong -> {
                            mRootView.rewardSuccess();
                        });
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
