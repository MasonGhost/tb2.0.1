package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.local.AccountBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.WalletRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.ErrorCodeConfig.DATA_HAS_BE_DELETED;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class LoginPresenter extends AppBasePresenter<LoginContract.Repository, LoginContract.View> implements LoginContract.Presenter {

    @Inject
    AuthRepository mAuthRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    AccountBeanGreenDaoImpl mAccountBeanGreenDao;
    @Inject
    WalletRepository mWalletRepository;

    @Inject
    public LoginPresenter(LoginContract.Repository repository, LoginContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void login(String phone, String password) {
        // 此处由于登陆方式有用户名和手机号还有邮箱 注册规则由服务器判断，所以我们不做判断处理
//        if (!RegexUtils.isMobileExact(phone) && !RegexUtils.isEmail(phone)) {
//            // 不符合手机号格式
//            mRootView.showErrorTips(mContext.getString(R.string.phone_number_toast_hint));
//            return;
//        }
        if (checkPasswordLength(password)) {
            return;
        }
        mRootView.setLogining();
        Subscription subscription = mRepository.loginV2(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

        addSubscrebe(subscription);
    }

    private void loginSuccess(AuthBean data) {
        mAuthRepository.clearAuthBean();
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
        mAccountBeanGreenDao.insertOrReplaceByName(mRootView.getAccountBean());
        mRootView.setLoginState(true);
    }

    @Override
    public List<AccountBean> getAllAccountList() {
        return mAccountBeanGreenDao.getMultiDataFromCache();
    }

    /**
     * 三方登录或者注册
     *
     * @param provider
     * @param access_token
     */
    @Override
    public void checkBindOrLogin(String provider, String access_token) {

        mUserInfoRepository.checkThridIsRegitser(provider, access_token)
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        loginSuccess(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (code == DATA_HAS_BE_DELETED) {
                            // 三方注册
                            mRootView.registerByThrid(provider,access_token);
                        } else {
                            // 登录失败
                            mRootView.setLoginState(false);
                            mRootView.showErrorTips(message);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginState(false);
                    }
                });

    }
    /**
     * 检查密码是否是最小长度
     *
     * @param password
     * @return
     */
    private boolean checkPasswordLength(String password) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showErrorTips(mContext.getString(R.string.password_toast_hint));
            return true;
        }
        return false;
    }
    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
    }

}
