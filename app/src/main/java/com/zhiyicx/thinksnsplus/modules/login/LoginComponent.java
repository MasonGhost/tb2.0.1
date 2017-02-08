package com.zhiyicx.thinksnsplus.modules.login;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = LoginPresenterModule.class)
public interface LoginComponent extends InjectComponent<LoginActivity> {
}
