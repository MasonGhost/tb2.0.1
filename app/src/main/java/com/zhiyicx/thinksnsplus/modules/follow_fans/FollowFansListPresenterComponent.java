package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = FollowFansListPresenterModule.class)
public interface FollowFansListPresenterComponent extends InjectComponent<FollowFansListFragment> {
}
