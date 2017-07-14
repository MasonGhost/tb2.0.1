package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.baseproject.config.PayConfig.MONEY_UNIT;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class MinePresenter extends BasePresenter<MineContract.Repository, MineContract.View> implements MineContract.Presenter {
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

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
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) authBean.getUser_id());
            if (userInfoBean != null) {
                WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(authBean.getUser_id());
                int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
                if (walletBean != null) {
                    walletBean.setBalance(walletBean.getBalance() * (ratio / MONEY_UNIT));
                    userInfoBean.setWallet(walletBean);
                }
                mRootView.setUserInfo(userInfoBean);
            }
            setMineTipVisable(false);
        }
    }

    /**
     * 用户信息在后台更新后，在该处进行刷新，这儿获取的是自己的用户信息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USERINFO_UPDATE)
    public void upDataUserInfo(List<UserInfoBean> data) {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (data != null) {
            for (UserInfoBean userInfoBean : data) {
                if (userInfoBean.getUser_id() == authBean.getUser_id()) {
                    userInfoBean.setWallet(mWalletBeanGreenDao.getSingleDataFromCacheByUserId(authBean.getUser_id()));
                    mRootView.setUserInfo(userInfoBean);
                    break;
                }
            }
        }
    }

    /**
     * 更新粉丝数量、系統消息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE)
    public void setMineTipVisable(boolean isVisiable) {
        // 关注消息
        FlushMessages followFlushMessages = mFlushMessageBeanGreenDao.getFlushMessgaeByKey(ApiConfig.NOTIFICATION_KEY_FOLLOWS);
        mRootView.setNewFollowTip(followFlushMessages != null ? followFlushMessages.getCount() : 0);
        // 系统消息
        FlushMessages systemInfoFlushMessages = mFlushMessageBeanGreenDao.getFlushMessgaeByKey(ApiConfig.NOTIFICATION_KEY_NOTICES);
        mRootView.setNewSystemInfo(systemInfoFlushMessages != null && systemInfoFlushMessages.getCount() > 0);
        //更新底部红点
        EventBus.getDefault().post((followFlushMessages != null && followFlushMessages.getCount() > 0) || (systemInfoFlushMessages != null && systemInfoFlushMessages.getCount() > 0), EventBusTagConfig.EVENT_IM_SET_MINE_TIP_VISABLE);
    }

    @Override
    public void readMessageByKey(String key) {
        mFlushMessageBeanGreenDao.readMessageByKey(key);
    }
}
