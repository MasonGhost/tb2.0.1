package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class IntegrationDetailPresenter extends AppBasePresenter<IntegrationDetailContract.View> implements IntegrationDetailContract.Presenter {


    @Inject
    BillRepository mBillRepository;

    @Inject
    public IntegrationDetailPresenter(IntegrationDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscribe = mBillRepository.integrationOrdersSuccess(TSListFragment.DEFAULT_PAGE_SIZE,maxId.intValue(), mRootView.getChooseType(),
                mRootView
                .getBillType())
                .subscribe(new BaseSubscribeForV2<List<RechargeSuccessV2Bean>>() {
                    @Override
                    protected void onSuccess(List<RechargeSuccessV2Bean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RechargeSuccessV2Bean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void selectBillByAction(int action) {
//        List<RechargeSuccessBean> data = mRechargeSuccessBeanGreenDao.selectBillByAction(action);
//        mRootView.onNetResponseSuccess(data, false);
    }

    @Override
    public void selectAll() {
        requestCacheData(1L, false);
    }


}
