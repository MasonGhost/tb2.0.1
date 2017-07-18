package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.local.RechargeSuccessBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillPresenter extends AppBasePresenter<BillContract.Repository, BillContract.View> implements BillContract.Presenter {

    @Inject
    RechargeSuccessBeanGreenDaoImpl mRechargeSuccessBeanGreenDao;

    @Inject
    public BillPresenter(BillContract.Repository repository, BillContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        mRepository.getBillList(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<RechargeSuccessBean>>() {
                    @Override
                    protected void onSuccess(List<RechargeSuccessBean> data) {
                        Collections.sort(data, new TimeStringSortClass());
                        removeAction(data, mRootView.getBillType());
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
    }

    @Override
    public List<RechargeSuccessBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        List<RechargeSuccessBean> data = mRechargeSuccessBeanGreenDao.getMultiDataFromCache();
        Collections.sort(data, new TimeStringSortClass());
        return data;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RechargeSuccessBean> data, boolean isLoadMore) {
        mRechargeSuccessBeanGreenDao.saveMultiData(data);
        return true;
    }

    @Override
    public void selectBillByAction(int action) {
        List<RechargeSuccessBean> data = mRechargeSuccessBeanGreenDao.selectBillByAction(action);
        mRootView.onNetResponseSuccess(data, false);
    }

    @Override
    public void selectAll() {
        List<RechargeSuccessBean> data = requestCacheData(1L, false);
        mRootView.onNetResponseSuccess(data, false);
    }

    public void removeAction(List<RechargeSuccessBean> list, int action) {
        if (action == 0)
            return;
        Iterator<RechargeSuccessBean> rechargesIterator = list.iterator();
        while (rechargesIterator.hasNext()) {
            RechargeSuccessBean data = rechargesIterator.next();
            if (data.getAction() != action) {
                rechargesIterator.remove();
            }
        }
    }
}
