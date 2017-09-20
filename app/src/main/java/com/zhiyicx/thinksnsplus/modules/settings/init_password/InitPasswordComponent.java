package com.zhiyicx.thinksnsplus.modules.settings.init_password;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/18
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = InitPasswordPresenterModule.class)
public interface InitPasswordComponent extends InjectComponent<InitPasswordActivity>{
}
