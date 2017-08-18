package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = QuestionCommentPresenterModule.class)
public interface QuestionCommentComponent extends InjectComponent<QuestionCommentActivity>{
}
