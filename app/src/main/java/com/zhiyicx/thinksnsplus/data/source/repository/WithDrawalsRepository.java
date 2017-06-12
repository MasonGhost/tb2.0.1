package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;
import com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.WithDrawalsConstract;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/05/23/14:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithDrawalsRepository implements WithDrawalsConstract.Repository {

    WalletClient mWalletClient;

    @Inject
    public WithDrawalsRepository(ServiceManager serviceManager) {
        mWalletClient = serviceManager.getWalletClient();
    }

    @Override
    public Observable<WithdrawResultBean> withdraw(double value, String type, String account) {
        return mWalletClient.withdraw(value, type, account);
    }
}
