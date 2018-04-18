package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = TBMarkDetailPresenterModule.class)
public interface TBMarkDetailComponent extends InjectComponent<TBMarkDetailActivity> {
}
