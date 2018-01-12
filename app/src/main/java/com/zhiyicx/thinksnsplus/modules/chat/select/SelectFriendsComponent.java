package com.zhiyicx.thinksnsplus.modules.chat.select;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SelectFriendsPresenterModule.class)
public interface SelectFriendsComponent extends InjectComponent<SelectFriendsActivity>{
}
