package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

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
public class WithdrawalsDetailPresenter extends AppBasePresenter<WithdrawalsDetailConstract.Repository, WithdrawalsDetailConstract.View>
        implements WithdrawalsDetailConstract.Presenter {

    @Inject
    public WithdrawalsDetailPresenter(WithdrawalsDetailConstract.Repository repository, WithdrawalsDetailConstract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = mRepository.getWithdrawListDetail(maxId.intValue())
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
    public List<WithdrawalsListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<WithdrawalsListBean> data, boolean isLoadMore) {
        return false;
    }
}
