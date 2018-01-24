package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/01/24/11:17
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = EditeInfoDetailPresenterModule.class)
public interface EditeInfoDetailComponent extends InjectComponent<EditeInfoDetailActivity> {
}
