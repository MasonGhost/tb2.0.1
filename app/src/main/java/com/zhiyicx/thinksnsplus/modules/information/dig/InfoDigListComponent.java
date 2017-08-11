package com.zhiyicx.thinksnsplus.modules.information.dig;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = InfoDigListPresenterModule.class)
public interface InfoDigListComponent extends InjectComponent<InfoDigListActivity>{
}
