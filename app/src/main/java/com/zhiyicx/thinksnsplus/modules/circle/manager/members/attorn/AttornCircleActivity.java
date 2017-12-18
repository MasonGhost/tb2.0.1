package com.zhiyicx.thinksnsplus.modules.circle.manager.members.attorn;

import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersListActivity;

/**
 * @Author Jliuer
 * @Date 2017/12/18/10:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AttornCircleActivity extends MembersListActivity {

    @Override
    protected AttornCircleFragment getFragment() {
        return AttornCircleFragment.newInstance(getIntent().getExtras());
    }
}
