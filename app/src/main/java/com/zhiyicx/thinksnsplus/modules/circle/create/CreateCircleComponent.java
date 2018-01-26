package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.circle.create.rule.RuleForCreateCircleActivity;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = CreateCirclePresenterModule.class)
public interface CreateCircleComponent extends InjectComponent<CreateCircleActivity> {
    void inject(RuleForCreateCircleActivity ruleForCreateCircleActivity);
}
