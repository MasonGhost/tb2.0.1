package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = RankTypeListPresenterModule.class)
public interface RankTypeListComponent extends InjectComponent<RankTypeListActivity>{
}
