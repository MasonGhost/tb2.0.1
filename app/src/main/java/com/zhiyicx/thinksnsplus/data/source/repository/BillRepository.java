package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillRepository implements BillContract.Repository {

    WalletClient mWalletClient;

    @Inject
    public BillRepository(ServiceManager serviceManager) {
        mWalletClient = serviceManager.getWalletClient();
    }

    @Override
    public Observable<List<RechargeSuccessBean>> getBillList(int after) {
        return mWalletClient.getRechargeSuccessList(TSListFragment.DEFAULT_PAGE_SIZE, after)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
