package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/04/26
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SystemConversationPresenterModule.class)
public interface SystemConversationComponent extends InjectComponent<SystemConversationActivity> {
}
