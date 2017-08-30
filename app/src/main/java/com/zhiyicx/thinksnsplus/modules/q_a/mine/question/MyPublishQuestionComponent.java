package com.zhiyicx.thinksnsplus.modules.q_a.mine.question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MyPublishQuestionPresenterModule.class)
public interface MyPublishQuestionComponent extends InjectComponent<MyPublishQuestionFragment>{
}
