package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.thinksnsplus.data.source.repository.LoginRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe loginPresenter的依赖对象
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@Module
public class LoginPresenterModule {
    private LoginContract.View mView;

    public LoginPresenterModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    LoginContract.View provideLoginContractView() {
        return mView;
    }

    @Provides
    LoginContract.Repository provideLoginRepository(LoginRepository loginRepository) {
        return loginRepository;
    }
}
