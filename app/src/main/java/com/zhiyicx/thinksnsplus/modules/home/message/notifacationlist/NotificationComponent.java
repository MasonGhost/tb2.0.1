package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = NotificationPresenterModule.class)
public interface NotificationComponent extends InjectComponent<NotificationActivity>{
}
