package com.zhiyicx.thinksnsplus.modules.circle.manager.members.blacklist;

import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersListActivity;

/**
 * @Author Jliuer
 * @Date 2018/01/05/9:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BlackListActivity extends MembersListActivity {

    @Override
    protected BlackListFragment getFragment() {
        return BlackListFragment.newInstance(getIntent().getExtras());
    }
}
