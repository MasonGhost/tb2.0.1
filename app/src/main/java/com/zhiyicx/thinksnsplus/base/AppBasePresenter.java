package com.zhiyicx.thinksnsplus.base;

import android.text.TextUtils;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/16
 * @Contact master.jungle68@gmail.com
 */

public abstract class AppBasePresenter<V extends IBaseView> extends BasePresenter<V> implements IBaseTouristPresenter {
    private static final String DEFAULT_WALLET_EXCEPTION_MESSAGE = "balance_check";
    @Inject
    protected AuthRepository mAuthRepository;
    @Inject
    CommentRepository mCommentRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    protected SystemRepository mSystemRepository;

    public AppBasePresenter(V rootView) {
        super(rootView);
    }

    @Override
    public boolean isTourist() {
        return mAuthRepository.isTourist();
    }

    @Override
    public boolean isLogin() {
        return mAuthRepository.isLogin();
    }

    @Override
    public boolean handleTouristControl() {
        if (isLogin()) {
            return false;
        } else {
            mRootView.showLoginPop();
            return true;
        }
    }

    protected Observable<Object> handleWalletBlance(long amount) {
        return mCommentRepository.getCurrentLoginUserInfo()
                .flatMap(userInfoBean -> {
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                    if (userInfoBean.getWallet() != null) {
                        mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                        if (userInfoBean.getWallet().getBalance() < amount) {
                            mRootView.goRecharge(WalletActivity.class);
                            return Observable.error(new RuntimeException(DEFAULT_WALLET_EXCEPTION_MESSAGE));
                        }
                    } else {
                        mRootView.goRecharge(WalletActivity.class);
                        return Observable.error(new RuntimeException(DEFAULT_WALLET_EXCEPTION_MESSAGE));
                    }
                    return Observable.just(userInfoBean);
                });
    }

    /**
     * 检查异常是否是手动抛出的余额检查异常，如果是不做处理，如果不是需要处理
     *
     * @param throwable 抛出的异常
     * @return
     */
    protected boolean isBalanceCheck(Throwable throwable) {
        if (throwable != null && !TextUtils.isEmpty(throwable.getMessage()) && DEFAULT_WALLET_EXCEPTION_MESSAGE.equals(throwable.getMessage())) {
            mRootView.dismissSnackBar();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SystemConfigBean getSystemConfigBean() {
        return mSystemRepository.getAppConfigInfoFromLocal();
    }

    @Override
    public String getGoldName() {
        try {
            return getSystemConfigBean().getSite().getGold_name().getName();

        } catch (Exception e) {
            return mContext.getResources().getString(R.string.defualt_golde_name);
        }
    }

    @Override
    public String getGoldUnit() {
        try {
            return getSystemConfigBean().getSite().getGold_name().getName();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getRatio() {
        return getSystemConfigBean().getWallet_ratio();
    }
}
