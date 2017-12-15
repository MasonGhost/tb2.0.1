package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.circle.manager.permission.PermissionFragment;

import dagger.Component;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {CircleDetailPresenterModule.class, ShareModule.class})
public interface CircleDetailComponent extends InjectComponent<CircleDetailActivity> {
    void inject(CircleDetailFragment circleDetailFragment);

    void inject(BaseCircleDetailFragment baseCircleDetailFragment);
}
