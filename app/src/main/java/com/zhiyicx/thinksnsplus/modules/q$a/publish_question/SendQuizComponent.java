package com.zhiyicx.thinksnsplus.modules.q$a.publish_question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SendQuizPresenterModule.class)
public interface SendQuizComponent extends InjectComponent<SendQuizActivity>{
}
