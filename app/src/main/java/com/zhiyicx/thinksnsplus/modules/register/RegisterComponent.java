package com.zhiyicx.thinksnsplus.modules.register;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = RegisterPresenterModule.class)
public interface RegisterComponent {
    void inject(RegisterActivity registerActivity);
}
