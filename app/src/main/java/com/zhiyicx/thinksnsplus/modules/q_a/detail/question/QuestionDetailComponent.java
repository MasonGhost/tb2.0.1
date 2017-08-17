package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {QuestionDetailPresenterModule.class, ShareModule.class})
public interface QuestionDetailComponent extends InjectComponent<QuestionDetailActivity>{
}
