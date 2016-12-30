package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.source.repository.LoginRepository;
import com.zhiyicx.thinksnsplus.modules.register.RegisterContract;

import javax.inject.Inject;

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

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
