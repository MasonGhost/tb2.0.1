package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.detail;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsDetailPresenter extends AppBasePresenter<WithdrawalsDetailConstract.Repository,WithdrawalsDetailConstract.View>
        implements WithdrawalsDetailConstract.Presenter {

    @Inject
    public WithdrawalsDetailPresenter(WithdrawalsDetailConstract.Repository repository, WithdrawalsDetailConstract.View rootView) {
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
    public boolean insertOrUpdateData(@NotNull List<WithdrawalsListBean> data, boolean isLoadMore) {
        return false;
    }
}
