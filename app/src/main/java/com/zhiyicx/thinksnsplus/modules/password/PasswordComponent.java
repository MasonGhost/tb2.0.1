package com.zhiyicx.thinksnsplus.modules.password;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordPresenterModule;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {ChangePasswordPresenterModule.class, FindPasswordPresenterModule.class})
public interface PasswordComponent {
    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(FindPasswordActivity findPasswordActivity);
}
