package com.zhiyicx.thinksnsplus.modules.tb.word;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

@FragmentScoped
@Component(modules = {WordPresenterModule.class},dependencies = AppComponent.class)
public interface WordComponent extends InjectComponent<WordActivity> {
}
