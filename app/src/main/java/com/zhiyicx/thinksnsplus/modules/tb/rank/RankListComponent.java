package com.zhiyicx.thinksnsplus.modules.tb.rank;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = RankListPresenterModule.class)
public interface RankListComponent extends InjectComponent<RankListActivity> {
}
