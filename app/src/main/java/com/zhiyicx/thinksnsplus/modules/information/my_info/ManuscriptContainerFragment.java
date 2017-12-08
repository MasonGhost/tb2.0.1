package com.zhiyicx.thinksnsplus.modules.information.my_info;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptListFragment.MY_INFO_TYPE_DONE;
import static com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptListFragment.MY_INFO_TYPE_ERROR;
import static com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptListFragment.MY_INFO_TYPE_ING;

/**
 * @Author Jliuer
 * @Date 2017/08/23/11:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ManuscriptContainerFragment extends TSViewPagerFragment {

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.info_published), getString(R.string.info_publishing), getString(R.string.info_publishrefuse));
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.my_info);
    }

    public static ManuscriptContainerFragment getInstance() {
        ManuscriptContainerFragment manuscriptContainerFragment = new ManuscriptContainerFragment();
        return manuscriptContainerFragment;
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Fragment publishedFragment = ManuscriptListFragment.getInstance(MY_INFO_TYPE_DONE);
            Fragment publishingFragment = ManuscriptListFragment.getInstance(MY_INFO_TYPE_ING);
            Fragment publishfailedFragment = ManuscriptListFragment.getInstance(MY_INFO_TYPE_ERROR);
            mFragmentList = Arrays.asList(publishedFragment, publishingFragment, publishfailedFragment);
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(0);
    }
}
