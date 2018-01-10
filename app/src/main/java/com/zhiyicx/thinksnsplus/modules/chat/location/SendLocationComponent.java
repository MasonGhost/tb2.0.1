package com.zhiyicx.thinksnsplus.modules.chat.location;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/10
 * @contact email:648129313@qq.com
 */
@FragmentScoped

@Component(dependencies = AppComponent.class,modules = SendLocationPresenterModule.class)
public interface SendLocationComponent extends InjectComponent<SendLocationActivity>{
}
