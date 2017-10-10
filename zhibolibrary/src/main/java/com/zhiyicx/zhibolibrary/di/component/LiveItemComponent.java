package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.LiveItemModule;
import com.zhiyicx.zhibolibrary.ui.fragment.LiveItemFragment;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = LiveItemModule.class, dependencies = ClientComponent.class)
public interface LiveItemComponent {
    void inject(LiveItemFragment fragment);
}