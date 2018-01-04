package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackActivity;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackPresenterModule;

import dagger.Component;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = ReportPresenterModule.class)
public interface ReportComponent extends InjectComponent<ReportActivity>{
}
