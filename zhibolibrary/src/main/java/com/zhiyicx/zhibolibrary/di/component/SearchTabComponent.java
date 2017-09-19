package com.zhiyicx.zhibolibrary.di.component;

import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.di.module.SearchTabModule;
import com.zhiyicx.zhibolibrary.ui.fragment.SearchTabFragement;

import dagger.Component;

/**
 * Created by zhiyicx on 2016/3/14.
 */
@ActivityScope
@Component(modules = SearchTabModule.class, dependencies = ClientComponent.class)
public interface SearchTabComponent {
    void inject(SearchTabFragement fragement);
}
