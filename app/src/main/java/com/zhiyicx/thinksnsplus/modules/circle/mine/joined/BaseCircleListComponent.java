package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = BaseCircleListPresenterModule.class)
public interface BaseCircleListComponent extends InjectComponent<BaseCircleListFragment>{
}
