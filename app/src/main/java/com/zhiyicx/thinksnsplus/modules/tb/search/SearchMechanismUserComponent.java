package com.zhiyicx.thinksnsplus.modules.tb.search;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SearchMechanismUserPresenterModule.class)
public interface SearchMechanismUserComponent extends InjectComponent<SearchMechanismUserFragment> {

}
