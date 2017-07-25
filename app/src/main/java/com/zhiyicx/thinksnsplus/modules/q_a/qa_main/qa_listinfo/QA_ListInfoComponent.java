package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = QA_listInfoFragmentPresenterModule.class)
public interface QA_ListInfoComponent extends InjectComponent<QA_ListInfoFragment> {
}
