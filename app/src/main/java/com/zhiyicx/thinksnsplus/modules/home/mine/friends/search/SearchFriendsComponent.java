package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SearchFriendsPresenterModule.class)
public interface SearchFriendsComponent extends InjectComponent<SearchFriendsActivity>{
}
