package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RewardPresenter extends AppBasePresenter<RewardContract.Repository, RewardContract.View> implements RewardContract.Presenter {
    public static final int DEFAULT_DELAY_TIME = 3;

    @Inject
    public RewardPresenter(RewardContract.Repository repository, RewardContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void reward(double rewardMoney, RewardType rewardType, long sourceId) {
        switch (rewardType) {
            case INFO:
                mRepository.rewardsInfo(sourceId, rewardMoney)
                        .subscribe(new BaseSubscribeForV2<Object>() {
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

                break;
            case DYNAMIC:

                break;

            default:

        }
    }
}
