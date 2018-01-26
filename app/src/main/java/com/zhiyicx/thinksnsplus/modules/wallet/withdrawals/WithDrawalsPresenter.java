package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
@SuppressWarnings("unchecked")
public class WithDrawalsPresenter extends AppBasePresenter< WithDrawalsConstract.View>
        implements WithDrawalsConstract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    BillRepository mBillRepository;

    @Inject
    public WithDrawalsPresenter(WithDrawalsConstract.View rootView) {
        super( rootView);
    }

    @Override
    public void withdraw(double value, String type, String account) {
        if (mRootView.getMoney() != (int) mRootView.getMoney()) {
            mRootView.initWithdrawalsInstructionsPop(R.string.withdrawal_instructions_detail);
            return;
        }

        if (value < PayConfig.realCurrency2GameCurrency(mRootView.getWalletConfigBean().getCase_min_amount(), getRatio())) {
            mRootView.minMoneyLimit();
            return;
        }
        value = PayConfig.gameCurrency2RealCurrency(value, getRatio());
        Subscription subscribe = mBillRepository.withdraw(value, type, account)
                .compose(mSchedulersTransformer)
                .doOnSubscribe(() -> {
                    mRootView.configSureBtn(false);
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.withdraw_doing));
                })
                .subscribe(new BaseSubscribeForV2<WithdrawResultBean>() {
                    @Override
                    protected void onSuccess(WithdrawResultBean data) {
                        mRootView.withdrawResult(data);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.withdraw_apply_succes));
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
