package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.os.Bundle;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CertificationDetailRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class MinePresenter extends AppBasePresenter<MineContract.Repository, MineContract.View> implements MineContract.Presenter {
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    CertificationDetailRepository mCertificationDetailRepository;

    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoGreenDao;

    @Inject
    public MinePresenter(MineContract.Repository repository, MineContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void getUserInfoFromDB() {
        // 尝试从数据库获取当前用户的信息
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getMyUserIdWithdefault());
            if (walletBean != null) {
                int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
///                    walletBean.setBalance(walletBean.getBalance() * (ratio / MONEY_UNIT));
                userInfoBean.setWallet(walletBean);
            }
            mRootView.setUserInfo(userInfoBean);
        }
        setMineTipVisable(false);
    }

    /**
     * 用户信息在后台更新后，在该处进行刷新，这儿获取的是自己的用户信息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USERINFO_UPDATE)
    public void upDataUserInfo(List<UserInfoBean> data) {
        Subscription subscribe = rx.Observable.just(data)
                .observeOn(Schedulers.io())
                .map(userInfoBeans -> {
                    if (userInfoBeans != null) {
                        for (UserInfoBean userInfoBean : userInfoBeans) {
                            if (userInfoBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                                userInfoBean.setWallet(mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getMyUserIdWithdefault()));
                                return userInfoBean;
                            }
                        }
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfoBean -> {
                    if (userInfoBean != null) {
                        mRootView.setUserInfo(userInfoBean);
                    }
                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);

    }

    /**
     * 更新粉丝数量、系統消息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE)
    public void setMineTipVisable(boolean isVisiable) {
        // 关注消息
        FlushMessages followFlushMessages = mFlushMessageBeanGreenDao.getFlushMessgaeByKey(NotificationConfig.NOTIFICATION_KEY_FOLLOWS);
        mRootView.setNewFollowTip(followFlushMessages != null ? followFlushMessages.getCount() : 0);
        // 系统消息
        FlushMessages systemInfoFlushMessages = mFlushMessageBeanGreenDao.getFlushMessgaeByKey(NotificationConfig.NOTIFICATION_KEY_NOTICES);
        mRootView.setNewSystemInfo(systemInfoFlushMessages != null && systemInfoFlushMessages.getCount() > 0);
        // 更新底部红点
        EventBus.getDefault().post((followFlushMessages != null && followFlushMessages.getCount() > 0) || (systemInfoFlushMessages != null &&
                systemInfoFlushMessages.getCount() > 0), EventBusTagConfig.EVENT_IM_SET_MINE_TIP_VISABLE);
    }

    @Override
    public void readMessageByKey(String key) {
        mFlushMessageBeanGreenDao.readMessageByKey(key);
    }

    /**
     * 更新用户信息
     */
    @Override
    public void updateUserInfo() {
        Subscription subscribe = mUserInfoRepository.getCurrentLoginUserInfo()
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if (data.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        mRootView.setUserInfo(data);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public int getBalanceRatio() {
        return mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
    }

    @Override
    public void getCertificationInfo() {
        Subscription subscribe = mCertificationDetailRepository.getCertificationInfo()
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<UserCertificationInfo>() {

                    @Override
                    protected void onSuccess(UserCertificationInfo data) {
                        mUserCertificationInfoGreenDao.insertOrReplace(data);
                        mRootView.updateCertification(data);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS)
    public void updateCertification(Bundle bundle) {
        if (bundle != null) {
            UserCertificationInfo info = bundle.getParcelable(EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS);
            mRootView.updateCertification(info);
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_CERTIFICATION_SUCCESS)
    public void sendSuccess(Bundle bundle) {
        if (bundle != null) {
            UserCertificationInfo info = bundle.getParcelable(EventBusTagConfig.EVENT_SEND_CERTIFICATION_SUCCESS);
            mRootView.updateCertification(info);
        }
    }

}
