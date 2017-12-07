package com.zhiyicx.thinksnsplus.modules.circle.search.container;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = CircleSearchContainerPresenterModule.class)
public interface CircleSearchContainerComponent extends InjectComponent<CircleSearchContainerActivity> {

}
