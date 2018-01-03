package com.zhiyicx.thinksnsplus.modules.circle.mine;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MyGroupPresenterModule.class)
public interface MyGroupComponent extends InjectComponent<MyGroupActivity>{
}
