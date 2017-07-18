package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
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

        return data.flatMap(new Func1<List<RechargeSuccessBean>, Observable<List<RechargeSuccessBean>>>() {
            @Override
            public Observable<List<RechargeSuccessBean>> call(List<RechargeSuccessBean> rechargeListBeen) {
                final List<Object> user_ids = new ArrayList<>();
                for (RechargeSuccessBean rechargeSuccessBean : rechargeListBeen) {
                    user_ids.add(rechargeSuccessBean.getUser_id());
                }
                return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                    for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                    }
                    for (int i = 0; i < rechargeListBeen.size(); i++) {
                        rechargeListBeen.get(i).setUserInfoBean(userInfoBeanSparseArray.get(rechargeListBeen.get(i).getUser_id().intValue()));
                    }
                    mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                    return rechargeListBeen;
                });
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
