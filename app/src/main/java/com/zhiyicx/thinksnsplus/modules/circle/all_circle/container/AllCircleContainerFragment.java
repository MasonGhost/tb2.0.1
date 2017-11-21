package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/11/21/14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerFragment extends TSViewPagerFragment<AllCircleContainerContract.Presenter>
        implements AllCircleContainerContract.View {

    @Override
    protected List<String> initTitles() {
        return null;
    }

    @Override
    protected List<Fragment> initFragments() {
        return null;
    }

    @Override
    protected void initData() {

    }
}
