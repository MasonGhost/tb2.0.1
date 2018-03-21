package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/21
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(modules = {DynamicCommentListPresenterModule.class}, dependencies = AppComponent.class)
public interface DynamicCommentListPresenterCompnent extends InjectComponent<DynamicCommentListActivity> {

}
