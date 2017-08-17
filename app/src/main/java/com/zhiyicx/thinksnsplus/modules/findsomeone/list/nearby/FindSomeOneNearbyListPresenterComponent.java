package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = FindSomeOneNearbyListPresenterModule.class)
public interface FindSomeOneNearbyListPresenterComponent extends InjectComponent<FindSomeOneNearbyListFragment> {
}
