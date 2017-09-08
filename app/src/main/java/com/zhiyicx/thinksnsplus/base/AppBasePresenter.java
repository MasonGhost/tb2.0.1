package com.zhiyicx.thinksnsplus.base;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
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

public abstract class AppBasePresenter<R, V extends IBaseView> extends BasePresenter<R, V> implements IBaseTouristPresenter {

    @Inject
    protected AuthRepository mAuthRepository;
    @Inject
    CommentRepository mCommentRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    public AppBasePresenter(R repository, V rootView) {
        super(repository, rootView);
    }

    public boolean istourist() {
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

    public  Observable<Object> handleWalletBlance(long amount) {
       return mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(com.zhiyicx.thinksnsplus.R
                        .string.transaction_doing)))
                .flatMap(new Func1<UserInfoBean, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(UserInfoBean userInfoBean) {
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        if (userInfoBean.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                            if (userInfoBean.getWallet().getBalance() < amount) {
                                mRootView.goRecharge(WalletActivity.class);
                                return Observable.error(new RuntimeException(""));
                            }
                        }
                        return Observable.just(userInfoBean);
                    }
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(com.zhiyicx.thinksnsplus.R.string.transaction_fail));
                    return null;
                }, () -> null);
    }
}
