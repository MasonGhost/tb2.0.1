package com.zhiyicx.thinksnsplus.modules.wallet;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class WalletPresenter extends AppBasePresenter<WalletContract.Repository, WalletContract.View> implements WalletContract.Presenter {
    public static final int DEFAULT_LOADING_SHOW_TIME = 2;

    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    private boolean mIsUsreInfoRequseted = false;// 用户信息是否拿到了

    @Inject
    public WalletPresenter(WalletContract.Repository repository, WalletContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void updateUserInfo() {
        Subscription timerSub = Observable.timer(DEFAULT_LOADING_SHOW_TIME, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        if (!mIsUsreInfoRequseted) {
                            mRootView.handleLoading(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }
                });
        Subscription userInfoSub = mUserInfoRepository.getCurrentLoginUserInfo()
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.handleLoading(false);
                        mIsUsreInfoRequseted = true;
                    }
                })
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {

                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if (data.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        mRootView.updateBalance(data.getWallet() != null ? data.getWallet().getBalance() : 0);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackWarningMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(timerSub);
        addSubscrebe(userInfoSub);
    }
}
