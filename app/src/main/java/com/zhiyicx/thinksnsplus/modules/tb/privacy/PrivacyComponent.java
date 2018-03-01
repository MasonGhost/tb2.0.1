package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/03/01/15:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = PrivacyPresenterModule.class)
public interface PrivacyComponent extends InjectComponent<PrivacyActivity> {
}
