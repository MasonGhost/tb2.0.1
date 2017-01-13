package com.zhiyicx.thinksnsplus.modules.password.changepassword;

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
@Component(dependencies = AppComponent.class, modules = ChangePasswordPresenterModule.class)
public interface ChangePasswordComponent {
    void inject(ChangePasswordActivity changePasswordActivity);
}
