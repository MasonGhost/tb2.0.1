package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/21
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SendDynamicPresenterModule.class)
public interface SendDynamicPresenterComponent extends InjectComponent<SendDynamicActivity> {

}
