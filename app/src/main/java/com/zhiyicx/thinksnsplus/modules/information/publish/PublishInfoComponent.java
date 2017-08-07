package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:57
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Component(dependencies = AppComponent.class,modules = PublishInfoPresenterModule.class)
public interface PublishInfoComponent extends InjectComponent<PublishInfoActivity> {
}
