package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/11/21/14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerFragment extends TSViewPagerFragment<AllCircleContainerContract.Presenter>
        implements AllCircleContainerContract.View {

    private List<String> mTitle;
    private List<Fragment> mFragments;
    public static final String RECOMMEND_INFO = "-1";

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_createcircle;
    }

    @Override
    protected int setRightLeftImg() {
        return R.mipmap.search_icon_search;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.all_group);
    }

    @Override
    protected List<String> initTitles() {
        if (mTitle == null) {
            mTitle = new ArrayList<>();
            mTitle.add(getString(R.string.info_recommend));
        }
        return mTitle;
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(CircleListFragment.newInstance(RECOMMEND_INFO));
        }
        return mFragments;
    }

    @Override
    protected void initData() {
        mTsvToolbar.setRightImg(R.mipmap.sec_nav_arrow,R.color.white);
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setAdjustMode(false);
    }
}
