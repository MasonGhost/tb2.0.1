package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.TBDynamicFragment;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterContainerFragment extends TSViewPagerFragment implements DynamicFragment.OnCommentClickListener {

    @BindView(R.id.mechainism_appbar_layout)
    AppBarLayout mAppBarLayout;

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
            mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS, this));
        }
        return mFragmentList;
    }

    @Override
    public void onButtonMenuShow(boolean isShow) {

    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);
        initListener();
    }

    private void initListener() {
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float statusBarHeight = (float) DeviceUtils.getStatuBarHeight(mActivity);
            float point = (float) Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
            float statusPer = statusBarHeight / (float) appBarLayout.getTotalScrollRange();
            float paddingTop = (float) appBarLayout.getTotalScrollRange() * (statusPer + point - 1);
            if (mVpFragment instanceof NoPullViewPager) {
                NoPullViewPager viewPager = (NoPullViewPager) mVpFragment;
                viewPager.setCanScroll(point == 1);
            }
            LogUtils.d(point);
            if (paddingTop < 0) {
                return;
            }
            mTsvToolbar.setPadding(0, (int) paddingTop, 0, 0);

        });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mechanismcenter;
    }

    @Override
    protected void initData() {

    }
}
