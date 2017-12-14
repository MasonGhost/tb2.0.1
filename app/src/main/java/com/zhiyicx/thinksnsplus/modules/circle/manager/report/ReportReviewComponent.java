package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:54
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = ReportReviewPresenterModule.class)
public interface ReportReviewComponent extends InjectComponent<ReportReviewActivity> {
}
