package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = DynamicCommentTollPresenterModule.class)
public interface DynamicCommentTollComponent extends InjectComponent<DynamicCommentTollActivity> {
}
