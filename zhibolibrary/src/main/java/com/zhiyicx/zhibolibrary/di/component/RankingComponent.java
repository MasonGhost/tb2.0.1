package com.zhiyicx.zhibolibrary.di.component;

import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.RankingModule;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLRankingActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = RankingModule.class, dependencies = ClientComponent.class)
public interface RankingComponent {
    void inject(ZBLRankingActivity activity);
}
