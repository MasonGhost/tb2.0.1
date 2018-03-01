package com.zhiyicx.thinksnsplus.modules.personal_center.tb;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterContainerFragment extends TSViewPagerFragment {

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isAdjustMode() {
        return true;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(mActivity.getResources().getStringArray(R.array
                .integration_mechanism_type));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(MechanismCenterFragment.newInstance());
            mFragmentList.add(MechanismCenterFragment.newInstance());
        }
        return mFragmentList;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mechanismcenter;
    }

    @Override
    protected void initData() {

    }
}
