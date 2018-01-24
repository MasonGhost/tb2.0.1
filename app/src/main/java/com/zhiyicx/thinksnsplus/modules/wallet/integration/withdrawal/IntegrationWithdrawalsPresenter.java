package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action0;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationWithdrawalsPresenter extends AppBasePresenter<IntegrationWithdrawalsContract.View> implements IntegrationWithdrawalsContract
        .Presenter {


    @Inject
    public IntegrationWithdrawalsPresenter(IntegrationWithdrawalsContract.View rootView) {
        super(rootView);
    }


    @Inject
    BillRepository mBillRepository;


    @Override
    public void integrationWithdrawals(@NotNull Integer amount) {

        Subscription subscribe = mBillRepository.integrationWithdrawals(amount)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.setSureBtEnable(true);
                    }
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.showSnackSuccessMessage(mContext.getResources().getString(R.string.integration_withdrawals_report_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackSuccessMessage(mContext.getResources().getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe);

    }
}
