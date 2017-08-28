package com.zhiyicx.thinksnsplus.modules.findsomeone.search.name;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SearchSomeOnePresenterModule.class)
public interface SearchSomeOneComponent extends InjectComponent<SearchSomeOneActivity> {

}
