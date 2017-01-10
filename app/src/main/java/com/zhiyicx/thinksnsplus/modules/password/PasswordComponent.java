package com.zhiyicx.thinksnsplus.modules.password;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = PasswordPresenterModule.class)
public interface PasswordComponent {
    void inject(PasswordActivity passwordActivity);
}
