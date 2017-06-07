package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import javax.inject.Inject;

import rx.functions.Action0;

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
    public void getPayStr(String channel, int amount) {
        mSystemRepository.getPayStr(channel, amount).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
            }
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
}
