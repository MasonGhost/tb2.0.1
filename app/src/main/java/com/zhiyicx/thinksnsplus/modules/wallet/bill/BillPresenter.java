package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillPresenter extends AppBasePresenter<BillContract.Repository,BillContract.View> implements BillContract.Presenter{

    @Inject
    public BillPresenter(BillContract.Repository repository, BillContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<WithdrawalsListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List data, boolean isLoadMore) {
        return false;
    }
}
