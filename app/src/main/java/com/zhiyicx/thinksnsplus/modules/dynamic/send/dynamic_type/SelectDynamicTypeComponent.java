package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SelectDynamicTypePresenterModule.class)
public interface SelectDynamicTypeComponent extends InjectComponent<SelectDynamicTypeActivity>{
}
