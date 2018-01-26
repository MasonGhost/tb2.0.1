package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class BindOldAccountPresenter extends BasePresenter<BindOldAccountContract.View>
        implements BindOldAccountContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    AuthRepository mAuthRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    BillRepository mWalletRepository;

    @Inject
    public BindOldAccountPresenter(
            BindOldAccountContract.View rootView) {
        super(rootView);
    }

    @Override
    public void bindAccount(ThridInfoBean thridInfoBean, String login, String password) {
        mRootView.setLogining();
        Subscription subscribe = mUserInfoRepository.bindWithInput(thridInfoBean.getProvider(), thridInfoBean.getAccess_token(), login, password)
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        loginSuccess(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 登录失败
                        mRootView.setLoginState(false);
                        mRootView.showErrorTips(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginState(false);
                    }
                });
        addSubscrebe(subscribe);
    }

    private void loginSuccess(AuthBean data) {
        mAuthRepository.clearAuthBean();
        mAuthRepository.clearThridAuth();
        // 登录成功跳转
        mAuthRepository.saveAuthBean(data);// 保存auth信息
        // IM 登录 需要 token ,所以需要先保存登录信息
        handleIMLogin();
        // 钱包信息我也不知道在哪儿获取
        mWalletRepository.getWalletConfigWhenStart(Long.parseLong(data.getUser_id() + ""));
        mUserInfoBeanGreenDao.insertOrReplace(data.getUser());
        if (data.getUser().getWallet() != null) {
            mWalletBeanGreenDao.insertOrReplace(data.getUser().getWallet());
        }
        mRootView.setLoginState(true);
    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig
                .GET_IM_INFO));
    }
}
