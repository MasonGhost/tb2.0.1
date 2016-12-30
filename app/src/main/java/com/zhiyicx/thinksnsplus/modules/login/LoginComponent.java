package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;

import javax.inject.Scope;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = LoginPresenterModule.class)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
