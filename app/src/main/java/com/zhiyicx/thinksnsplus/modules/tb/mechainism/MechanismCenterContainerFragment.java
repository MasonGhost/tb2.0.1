package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.TBDynamicFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterContainerFragment extends TSViewPagerFragment implements DynamicFragment.OnCommentClickListener {

    @BindView(R.id.mechainism_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.iv_head)
    UserAvatarView mIvHeadIcon;
    @BindView(R.id.tv_name)
    TextView mTvUserName;
    @BindView(R.id.tv_dec)
    TextView mTvDes;
    Unbinder unbinder;


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
            mFragmentList.add(MechanismCenterFragment.newInstance(getArguments()));
            mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this, getArguments().getParcelable(PersonalCenterFragment
                    .PERSONAL_CENTER_DATA)));
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
        UserInfoBean userInfoBean = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
        ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvHeadIcon);
        // 设置用户名
        mTvUserName.setText(userInfoBean.getName());
        mTvDes.setText(userInfoBean.getIntro());
    }

    public static MechanismCenterContainerFragment newInstance(Bundle bundle) {
        MechanismCenterContainerFragment mechanismCenterContainerFragment = new MechanismCenterContainerFragment();
        mechanismCenterContainerFragment.setArguments(bundle);
        return mechanismCenterContainerFragment;
    }


    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_more:

                break;
                default:
        }
    }
}
