package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletConfigBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.WalletClient;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBillRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository.MAX_RETRY_COUNTS;
import static com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository.RETRY_DELAY_TIME;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillRepository implements IBillRepository {

    WalletClient mWalletClient;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    WalletConfigBeanGreenDaoImpl mWalletConfigBeanGreenDao;

    @Inject
    public BillRepository(ServiceManager serviceManager) {
        mWalletClient = serviceManager.getWalletClient();
    }

    @Override
    public Observable<List<RechargeSuccessBean>> getBillList(int after) {
        return dealRechargeList(mWalletClient.getRechargeSuccessList(TSListFragment.DEFAULT_PAGE_SIZE, after));
    }

    @Override
    public Observable<List<RechargeSuccessBean>> dealRechargeList(Observable<List<RechargeSuccessBean>> data) {

        return data
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<RechargeSuccessBean>, Observable<List<RechargeSuccessBean>>>() {
                    @Override
                    public Observable<List<RechargeSuccessBean>> call(List<RechargeSuccessBean> rechargeListBeen) {
                        final List<Object> user_ids = new ArrayList<>();
                        for (RechargeSuccessBean rechargeSuccessBean : rechargeListBeen) {
                            if (rechargeSuccessBean.getChannel().equals("user") || rechargeSuccessBean.getChannel().equals("system")) //
                                // @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/wallet/charge.md}
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


    @Override
    public Observable<WalletConfigBean> getWalletConfig() {
        return mWalletClient.getWalletConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getWalletConfigWhenStart(Long user_id) {
        getWalletConfig()
                .observeOn(Schedulers.io())
                .subscribe(new BaseSubscribeForV2<WalletConfigBean>() {
                    @Override
                    protected void onSuccess(WalletConfigBean data) {
                        data.setUser_id(user_id);
                        mWalletConfigBeanGreenDao.insertOrReplace(data);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
    }


    @Override
    public Observable<RechargeSuccessBean> rechargeSuccess(String charge) {
        return mWalletClient.rechargeSuccess(charge)
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RechargeSuccessBean> rechargeSuccessCallBack(String charge) {
        return mWalletClient.rechargeSuccessCallBack(charge)
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<WithdrawResultBean> withdraw(double value, String type, String account) {
        return mWalletClient.withdraw((long) value, type, account);
    }

    /**
     * @param after 获取更多数据，上一次获取列表的最后一条 ID
     * @return
     */
    @Override
    public Observable<List<WithdrawalsListBean>> getWithdrawListDetail(int after) {
        return mWalletClient.getWithdrawList(TSListFragment.DEFAULT_PAGE_SIZE, after)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
