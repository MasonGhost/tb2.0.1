package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = CircleDetailPresenterModule.class)
public interface CircleDetailComponent extends InjectComponent<CircleDetailActivity> {
}
