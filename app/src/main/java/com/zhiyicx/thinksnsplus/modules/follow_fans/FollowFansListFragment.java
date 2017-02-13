package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSFragment<FollowFansListPresenter> {
    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_follow_fans_list;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }
}
