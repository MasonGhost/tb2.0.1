package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import javax.inject.Inject;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RechargePresenter extends AppBasePresenter<RechargeContract.Repository, RechargeContract.View> implements RechargeContract.Presenter {

    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public RechargePresenter(RechargeContract.Repository repository, RechargeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getPayStr(String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mSystemRepository.getPayStr(channel, amount).doOnSubscribe(() -> {
            mRootView.configSureBtn(false);
            mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
        }).subscribe(new BaseSubscribeForV2<PayStrBean>() {
            @Override
            protected void onSuccess(PayStrBean data) {
                mRootView.showSnackSuccessMessage(mContext.getString(R.string.recharge_credentials_succes));
                mRootView.payCredentialsResult(data);
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
    public void rechargeSuccess(String charge) {
        mRepository.rechargeSuccess(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
            @Override
            protected void onSuccess(RechargeSuccessBean data) {
                rechargeSuccessCallBack(data.getId() + "");
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void rechargeSuccessCallBack(String charge) {
        mRepository.rechargeSuccessCallBack(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
            @Override
            protected void onSuccess(RechargeSuccessBean data) {
                mRootView.rechargeSuccess(data);
            }
        });
    }
}
