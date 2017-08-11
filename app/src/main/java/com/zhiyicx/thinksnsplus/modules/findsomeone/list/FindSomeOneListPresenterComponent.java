package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = FindSomeOneListPresenterModule.class)
public interface FindSomeOneListPresenterComponent extends InjectComponent<FindSomeOneListFragment> {
}
