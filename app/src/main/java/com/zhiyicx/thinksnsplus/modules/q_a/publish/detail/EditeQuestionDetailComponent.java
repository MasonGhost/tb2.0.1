package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.q_a.answer.EditeAnswerDetailActivity;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules = EditeQuestionDetailPresenterModule.class,dependencies = AppComponent.class)
public interface EditeQuestionDetailComponent extends InjectComponent<EditeQuestionDetailActivity> {
    void inject(EditeAnswerDetailActivity answerDetailActivity);
}
