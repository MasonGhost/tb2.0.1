package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = InfoSearchPresenterMudule.class)
public interface InfoSearchComponent extends InjectComponent<SearchActivity> {

}
