package com.zhiyicx.thinksnsplus.modules.draftbox;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/22/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = DraftBoxPresenterModule.class)
public interface DraftBoxComponent extends InjectComponent<DraftBoxFragment>{
}
