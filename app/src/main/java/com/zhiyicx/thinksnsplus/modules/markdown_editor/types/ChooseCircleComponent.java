package com.zhiyicx.thinksnsplus.modules.markdown_editor.types;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/12/15/18:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ChooseCirclePresenterModule.class)
public interface ChooseCircleComponent extends InjectComponent<ChooseCircleActivity> {
}
