package com.zhiyicx.thinksnsplus.modules.settings.bind;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = AccountBindPresenterModule.class)
public interface AccountBindComponent extends InjectComponent<AccountBindActivity>{
}
