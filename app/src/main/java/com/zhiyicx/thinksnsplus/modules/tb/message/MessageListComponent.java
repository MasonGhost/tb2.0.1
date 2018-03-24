package com.zhiyicx.thinksnsplus.modules.tb.message;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * Created by lx on 2018/3/24.
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MessageListPresenterModule.class)
public interface MessageListComponent extends InjectComponent<MessageListFragment> {
}
