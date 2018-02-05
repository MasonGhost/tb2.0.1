package com.zhiyicx.thinksnsplus.modules.chat.v2;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ChatPresenterModuleV2.class)
public interface ChatComponentV2 extends InjectComponent<ChatActivityV2> {
}
