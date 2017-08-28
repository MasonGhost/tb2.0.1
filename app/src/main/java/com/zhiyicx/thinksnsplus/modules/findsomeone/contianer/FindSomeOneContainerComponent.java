package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOneActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOnePresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = FindSomeOneContainerPresenterModule.class)
public interface FindSomeOneContainerComponent extends InjectComponent<FindSomeOneContainerActivity> {

}
