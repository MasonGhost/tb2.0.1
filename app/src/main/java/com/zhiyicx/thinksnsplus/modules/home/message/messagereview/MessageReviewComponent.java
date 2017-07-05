package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MessageLikePresenterModule.class)
public interface MessageLikeComponent extends InjectComponent<MessageLikeActivity>{
}
