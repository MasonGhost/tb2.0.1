package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletPresenter extends AppBasePresenter<WalletContract.View> implements WalletContract.Presenter {

    @Inject
    public WalletPresenter(WalletContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<WalletData> data, boolean isLoadMore) {
        return false;
    }
}
