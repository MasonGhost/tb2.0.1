package com.zhiyicx.thinksnsplus.modules.circle.mine.container;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.BaseCircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.MyJoinedCircleFragment;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.MyWaitAuditCircleFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyCirclePostContentContainerFragment extends TSViewPagerFragment {

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean isAdjustMode() {
        return false;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getResources().getStringArray(R.array.circle_post_mine_type));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(BaseCircleDetailFragment.newInstance(BaseCircleRepository.CircleMinePostType.PUBLISH));
            mFragmentList.add(BaseCircleDetailFragment.newInstance(BaseCircleRepository.CircleMinePostType.HAD_PINNED));
            mFragmentList.add(BaseCircleDetailFragment.newInstance(BaseCircleRepository.CircleMinePostType.WAIT_PINNED_AUDIT));
        }
        return mFragmentList;
    }


    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.showDivider(false);
        mTsvToolbar.setPadding(getResources().getDimensionPixelOffset(R.dimen.spacing_mid), 0, getResources().getDimensionPixelOffset(R.dimen
                .spacing_mid), 0);
    }

    @Override
    protected void initData() {

    }
}
