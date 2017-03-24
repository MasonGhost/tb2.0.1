package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules = {ShareModule.class,
        InfoDetailsPresenterMudule.class},dependencies = AppComponent.class)
interface InfoDetailsComponent extends InjectComponent<InfoDetailsActivity> {
}
