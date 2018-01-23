package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.information.publish.uploadcover.UploadCoverActivity;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:57
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = PublishInfoPresenterModule.class)
public interface PublishInfoComponent extends InjectComponent<UploadCoverActivity> {
}
