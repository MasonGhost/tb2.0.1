package com.zhiyicx.thinksnsplus.modules.home.find;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * Created by Administrator on 2018/4/18.
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {FindPresenterModule.class})
public interface FindComponent extends InjectComponent<FindFragment> {
}
