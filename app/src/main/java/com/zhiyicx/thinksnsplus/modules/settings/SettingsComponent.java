<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.settings;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SettingsPresenterModule.class)
public interface SettingsComponent extends InjectComponent<SettingsActivity> {
}
=======
package com.zhiyicx.thinksnsplus.modules.settings;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/28
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SettingsPresenterModule.class)
public interface SettingsComponent extends InjectComponent<SettingsActivity> {
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
