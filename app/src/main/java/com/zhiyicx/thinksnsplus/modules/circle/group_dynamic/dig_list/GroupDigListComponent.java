package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(modules = GroupDigListPresenterModule.class, dependencies = AppComponent.class)
public interface GroupDigListComponent extends InjectComponent<GroupDigListActivity>{
}
