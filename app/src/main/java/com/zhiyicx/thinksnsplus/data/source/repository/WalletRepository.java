package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */

public class WalletRepository implements WalletContract.Repository {


    WalletClient mWalletClient;

    @Inject
    public WalletRepository(ServiceManager serviceManager) {
        this.mWalletClient = serviceManager.getWalletClient();
    }

    @Override
    public Observable<WalletConfigBean> getWalletConfig() {
        return mWalletClient.getWalletConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
