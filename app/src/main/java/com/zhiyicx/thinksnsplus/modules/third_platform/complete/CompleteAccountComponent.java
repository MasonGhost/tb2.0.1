package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

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
@Component(dependencies = AppComponent.class, modules = CompleteAccountPresenterModule.class)
public interface CompleteAccountComponent extends InjectComponent<CompleteAccountActivity>{
}
