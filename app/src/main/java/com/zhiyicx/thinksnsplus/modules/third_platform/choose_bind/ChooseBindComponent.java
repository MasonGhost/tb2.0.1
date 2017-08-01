package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ChooseBindPresenterModule.class)
public interface ChooseBindComponent extends InjectComponent<ChooseBindActivity>{
}
