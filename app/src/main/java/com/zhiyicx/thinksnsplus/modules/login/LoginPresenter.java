package com.zhiyicx.thinksnsplus.modules.login;

import android.widget.Toast;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.repository.LoginRepository;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

import java.util.logging.Logger;

import javax.inject.Inject;

import rx.Subscriber;
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
        mRepository.login(mContext, phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<AuthBean>>() {
                    @Override
                    public void call(BaseJson<AuthBean> integerBaseJson) {
                        if (integerBaseJson.isStatus()) {
                            // 登录成功跳转
                            mRootView.setLoginSuccess();
                        } else {
                            // 登录失败
                            mRootView.setLoginFailure();
                            mRootView.showErrorTips(integerBaseJson.getMessage());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable e) {
                        LogUtils.e(e, "login_error" + e.getMessage());
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginFailure();
                    }
                });
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
