<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = GuidePresenterModule.class)
public interface GuideComponent extends InjectComponent<GuideActivity> {
}
=======
package com.zhiyicx.thinksnsplus.modules.guide;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = GuidePresenterModule.class)
public interface GuideComponent extends InjectComponent<GuideActivity> {
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
