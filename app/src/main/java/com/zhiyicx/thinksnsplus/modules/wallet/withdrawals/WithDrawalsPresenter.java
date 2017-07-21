package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action0;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
@SuppressWarnings("unchecked")
public class WithDrawalsPresenter extends AppBasePresenter<WithDrawalsConstract.Repository, WithDrawalsConstract.View>
        implements WithDrawalsConstract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public WithDrawalsPresenter(WithDrawalsConstract.Repository repository, WithDrawalsConstract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void withdraw(double value, String type, String account) {
        if (mRootView.getMoney() != (int) mRootView.getMoney()) {
            mRootView.initWithdrawalsInstructionsPop(R.string.withdrawal_instructions_detail);
            return;
        }

        if (value < mRootView.getWalletConfigBean().getCase_min_amount() / PayConfig.MONEY_UNIT) {
            mRootView.minMoneyLimit();
            return;
        }
        value = value * PayConfig.MONEY_UNIT;
        Subscription subscribe = mRepository.withdraw(value, type, account)
                .compose(mSchedulersTransformer)
                .doOnSubscribe(() -> {
                    mRootView.configSureBtn(false);
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.withdraw_doing));
                })
                .subscribe(new BaseSubscribeForV2<WithdrawResultBean>() {
                    @Override
                    protected void onSuccess(WithdrawResultBean data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.withdraw_succes));
                        mRootView.withdrawResult(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.withdraw_failed));
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.configSureBtn(true);
                    }
                });
        addSubscrebe(subscribe);
    }
}
