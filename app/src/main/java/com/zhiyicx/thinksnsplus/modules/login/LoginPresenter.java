package com.zhiyicx.thinksnsplus.modules.login;

import android.widget.Toast;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IAuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.LoginRepository;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.logging.Logger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
                .subscribe(new Action1<BaseJson<AuthBean>>() {
                    @Override
                    public void call(BaseJson<AuthBean> integerBaseJson) {
                        if (integerBaseJson.isStatus()) {
                            // 登录成功跳转
                            mAuthRepository.saveAuthBean(integerBaseJson.getData());// 保存auth信息
                            // 刷新im信息
                            BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
                            mRootView.setLoginState(true);
                        } else {
                            // 登录失败
                            mRootView.setLoginState(false);
                            mRootView.showErrorTips(integerBaseJson.getMessage());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable e) {
                        LogUtils.e(e, "login_error" + e.getMessage());
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginState(false);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void onStart() {

    }

}
