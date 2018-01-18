package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MyFriendsListPresenterModule.class)
public interface MyFriendsListComponent extends InjectComponent<MyFriendsListActivity>{
}
