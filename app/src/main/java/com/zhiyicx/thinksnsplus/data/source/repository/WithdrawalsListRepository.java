package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.list_detail.WithdrawalsDetailConstract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsListRepository implements WithdrawalsDetailConstract.Repository {

    WalletClient mWalletClient;

    @Inject
    public WithdrawalsListRepository(ServiceManager serviceManager) {
        mWalletClient = serviceManager.getWalletClient();
    }

    @Override
    public Observable<List<WithdrawalsListBean>> getWithdrawListDetail(int after) {
        return mWalletClient.getWithdrawList(TSListFragment.DEFAULT_PAGE_SIZE, after)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
