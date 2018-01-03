package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.source.local.WithdrawalsListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsDetailPresenter extends AppBasePresenter<WithdrawalsDetailConstract.View>
        implements WithdrawalsDetailConstract.Presenter {

    @Inject
    WithdrawalsListBeanGreenDaoImpl mWithdrawalsListBeanGreenDao;
    @Inject
    BillRepository mBillRepository;

    @Inject
    public WithdrawalsDetailPresenter(WithdrawalsDetailConstract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = mBillRepository.getWithdrawListDetail(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<WithdrawalsListBean>>() {
                    @Override
                    protected void onSuccess(List<WithdrawalsListBean> data) {
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
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(mWithdrawalsListBeanGreenDao.getMultiDataFromCache(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<WithdrawalsListBean> data, boolean isLoadMore) {
        mWithdrawalsListBeanGreenDao.saveMultiData(data);
        return true;
    }
}
