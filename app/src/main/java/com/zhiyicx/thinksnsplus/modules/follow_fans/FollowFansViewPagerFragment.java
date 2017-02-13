package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.widget.TabSelectView;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansViewPagerFragment extends TSFragment<FollowFansListContract.Presenter> {
    @BindView(R.id.tsv_toolbar)
    TabSelectView mTsvToolbar;
    @BindView(R.id.vp_fragment)
    ViewPager mVpFragment;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_follow_fans_viewpager;
    }

    @Override
    protected void initView(View rootView) {
        initViewPager();
        mTsvToolbar.initTabView(mVpFragment, initTitles());
        mTsvToolbar.setLeftClickListener(this, new TabSelectView.TabLeftRightClickListener() {
            @Override
            public void buttonClick() {
                getActivity().finish();
            }
        });
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initData() {

    }

    private List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.fans));
        return titles;
    }

    private void initViewPager() {
        TSViewPagerAdapter tsViewPagerAdapter = new TSViewPagerAdapter(getFragmentManager());
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(FollowFansListFragment.initFragment(null));
        fragmentList.add(FollowFansListFragment.initFragment(null));
        tsViewPagerAdapter.bindData(fragmentList);
        mVpFragment.setAdapter(tsViewPagerAdapter);
    }

    public static FollowFansViewPagerFragment initFragment(Bundle bundle) {
        FollowFansViewPagerFragment followFansViewPagerFragment = new FollowFansViewPagerFragment();
        followFansViewPagerFragment.setArguments(bundle);
        return followFansViewPagerFragment;
    }

}
