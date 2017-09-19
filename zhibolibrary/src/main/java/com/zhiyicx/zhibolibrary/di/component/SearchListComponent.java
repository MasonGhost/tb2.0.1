package com.zhiyicx.zhibolibrary.di.component;


import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.UserHomeModule;
import com.zhiyicx.zhibolibrary.ui.holder.SearchListHolder;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = UserHomeModule.class, dependencies = ClientComponent.class)
public interface SearchListComponent {
    void inject(SearchListHolder searchListHolder);
}
