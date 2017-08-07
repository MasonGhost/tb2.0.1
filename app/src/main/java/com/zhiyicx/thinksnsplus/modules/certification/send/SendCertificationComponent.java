package com.zhiyicx.thinksnsplus.modules.certification.send;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SendCertificationPresenterModule.class)
public interface SendCertificationComponent extends InjectComponent<SendCertificationActivity>{
}
