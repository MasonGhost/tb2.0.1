package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.EndStreamModule;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLEndStreamingActivity;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = EndStreamModule.class, dependencies = ClientComponent.class)
public interface EndStreamComponent {
    void inject(ZBLEndStreamingActivity activity);
}
