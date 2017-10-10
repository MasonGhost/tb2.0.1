package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public BillRepository(ServiceManager serviceManager) {
        mWalletClient = serviceManager.getWalletClient();
    }

    @Override
    public Observable<List<RechargeSuccessBean>> getBillList(int after) {
        return dealRechargeList(mWalletClient.getRechargeSuccessList(TSListFragment.DEFAULT_PAGE_SIZE, after));
    }

    public Observable<List<RechargeSuccessBean>> dealRechargeList(Observable<List<RechargeSuccessBean>> data) {

        return data
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<RechargeSuccessBean>, Observable<List<RechargeSuccessBean>>>() {
            @Override
            public Observable<List<RechargeSuccessBean>> call(List<RechargeSuccessBean> rechargeListBeen) {
                final List<Object> user_ids = new ArrayList<>();
                for (RechargeSuccessBean rechargeSuccessBean : rechargeListBeen) {
                    if (rechargeSuccessBean.getChannel().equals("user") || rechargeSuccessBean.getChannel().equals("system")) // @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/wallet/charge.md}
                    {
                        user_ids.add(rechargeSuccessBean.getAccount());
                        try {
                            rechargeSuccessBean.setUser_id(Long.valueOf(rechargeSuccessBean.getAccount()));
                        } catch (Exception e) {
                        }
                    } else {
                        user_ids.add(rechargeSuccessBean.getUser_id());
                    }

                }
                return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                    mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                    return rechargeListBeen;
                });
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
