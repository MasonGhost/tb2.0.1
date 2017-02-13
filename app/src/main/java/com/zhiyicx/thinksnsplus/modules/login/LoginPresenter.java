package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public class LoginPresenter extends BasePresenter<LoginContract.Repository, LoginContract.View> implements LoginContract.Presenter {

    @Inject
    public LoginPresenter(LoginContract.Repository repository, LoginContract.View rootView) {
        super(repository, rootView);
    }

    @Inject
    IAuthRepository mAuthRepository;

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void login(String phone, String password) {
        if (!RegexUtils.isMobileExact(phone)) {
            // 不符合手机号格式
            mRootView.showErrorTips(mContext.getString(R.string.phone_number_toast_hint));
            return;
        }
        mRootView.setLogining();
        Subscription subscription = mRepository.login(mContext, phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        // 登录成功跳转
                        mAuthRepository.saveAuthBean(data);// 保存auth信息
                        // 获取用户信息
                        getUserInfo(data);
                        // IM 登录 需要 token ,所以需要先保存登录信息
                        handleIMLogin();
                        mRootView.setLoginState(true);
                    }

                    @Override
                    protected void onFailure(String message) {
                        // 登录失败
                        mRootView.setLoginState(false);
                        mRootView.showErrorTips(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, "login_error" + throwable.getMessage());
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginState(false);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void onStart() {

    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
    }

    private void getUserInfo(AuthBean data) {
        HashMap<String, Object> userInfoParams = new HashMap<>();
        userInfoParams.put("user_id", data.getUser_id());
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_USER_INFO, userInfoParams));
    }

}
