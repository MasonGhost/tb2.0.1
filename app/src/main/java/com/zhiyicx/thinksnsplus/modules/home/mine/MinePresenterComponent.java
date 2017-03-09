package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import javax.inject.Inject;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(modules = MinePresenterModule.class, dependencies = AppComponent.class)
public interface MinePresenterComponent extends InjectComponent<MineFragment> {
}
