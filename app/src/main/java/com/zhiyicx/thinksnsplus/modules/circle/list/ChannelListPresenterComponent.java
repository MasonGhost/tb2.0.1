package com.zhiyicx.thinksnsplus.modules.circle.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(modules = ChannelListPresenterModule.class, dependencies = AppComponent.class)
public interface ChannelListPresenterComponent extends InjectComponent<ChannelListFragment> {
}
