package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = QASearchContainerPresenterModule.class)
public interface QASearchContainerComponent extends InjectComponent<QASearchContainerActivity> {

}
