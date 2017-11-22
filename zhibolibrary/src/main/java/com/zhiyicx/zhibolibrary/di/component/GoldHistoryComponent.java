package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.GoldHistoryModule;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLGoldHistoryActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = GoldHistoryModule.class, dependencies = ClientComponent.class)
public interface GoldHistoryComponent {
    void inject(ZBLGoldHistoryActivity activity);
}
