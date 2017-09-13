package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(modules = HeadPortraitViewPresenterModule.class, dependencies = AppComponent.class)
public interface HeadPortraitViewComponent extends InjectComponent<HeadPortraitViewActivity>{
}
