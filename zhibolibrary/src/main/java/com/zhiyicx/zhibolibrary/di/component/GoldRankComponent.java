package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.GoldRankModule;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLGoldRankActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = GoldRankModule.class, dependencies = ClientComponent.class)
public interface GoldRankComponent {
    void inject(ZBLGoldRankActivity activity);
}
