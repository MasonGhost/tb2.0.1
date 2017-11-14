package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list.GroupDigListFragment.GROUP_DIG_LIST_DATA;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */

public class GroupDigListActivity extends TSActivity<GroupDigListPresenter, GroupDigListFragment>{

    @Override
    protected GroupDigListFragment getFragment() {
        return GroupDigListFragment.instance(getIntent().getBundleExtra(GROUP_DIG_LIST_DATA));
    }

    @Override
    protected void componentInject() {
        DaggerGroupDigListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .groupDigListPresenterModule(new GroupDigListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
