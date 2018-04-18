package com.zhiyicx.thinksnsplus.modules.tb.exchange;

/**
 * Created by Administrator on 2018/4/17.
 */

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
@Component(dependencies = AppComponent.class, modules = ExchangePresenterModule.class)
public interface ExchangeComponent extends InjectComponent<ExchangeActivity> {
}
