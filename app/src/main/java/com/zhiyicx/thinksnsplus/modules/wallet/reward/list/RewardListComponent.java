package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.rank.RankActivity;
import com.zhiyicx.thinksnsplus.modules.rank.RankPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = RewardListPresenterModule.class)
public interface RewardListComponent extends InjectComponent<RewardListActivity> {
}
