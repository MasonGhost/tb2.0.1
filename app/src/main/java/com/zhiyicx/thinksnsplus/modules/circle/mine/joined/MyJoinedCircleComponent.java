package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListFragment;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = MyJoinedCirclePresenterModule.class)
public interface MyJoinedCircleComponent extends InjectComponent<MyJoinedCircleFragment>{
}
