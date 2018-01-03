package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.widget.TabSelectView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListFragment;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.create.types.CircleTyepsActivity;
import com.zhiyicx.thinksnsplus.modules.circle.search.container.CircleSearchContainerActivity;
import com.zhiyicx.thinksnsplus.modules.circle.search.container.CircleSearchContainerViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_CERTIFICATION_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/11/21/14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerFragment extends TSViewPagerFragment<AllCircleContainerContract.Presenter>
        implements AllCircleContainerContract.View {

    private List<String> mTitle;

    // 推荐专用
    public static final String RECOMMEND_INFO = "-1";


    private UserCertificationInfo mUserCertificationInfo;

    // 提示需要认证的
    private ActionPopupWindow mCertificationAlertPopWindow;


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
        return R.mipmap.ico_search;
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
    public void setCategroiesList(List<CircleTypeBean> circleTypeList) {
        for (CircleTypeBean circleTypeBean : circleTypeList) {
            if (RECOMMEND_INFO.equals(circleTypeBean.getId().intValue() + "")) {
                continue;
            }
            mTitle.add(circleTypeBean.getName());
            mFragmentList.add(CircleListFragment.newInstance(circleTypeBean.getId() + ""));
        }
        mTsvToolbar.notifyDataSetChanged(mTitle);
        tsViewPagerAdapter.bindData(mFragmentList, mTitle.toArray(new String[]{}));
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        // 发布提示 1、首先需要认证 2、需要付费
        if (mPresenter.handleTouristControl()) {
            return;
        }
        mPresenter.checkCertification();

    }

    @Override
    protected void setRightLeftClick() {
        super.setRightLeftClick();
        CircleSearchContainerActivity.startCircelSearchActivity(mActivity, CircleSearchContainerViewPagerFragment.PAGE_CIRCLE);
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(CircleListFragment.newInstance(RECOMMEND_INFO));
        }
        return mFragmentList;
    }

    @Override
    protected void initViewPager(View rootView) {
        mTsvToolbar = (TabSelectView) rootView.findViewById(com.zhiyicx.baseproject.R.id.tsv_toolbar);
        mTsvToolbar.setRightImg(R.mipmap.sec_nav_arrow, R.color.white);
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setDefaultTabLinehegiht(R.integer.no_line_height);
        mTsvToolbar.showDivider(false);
        mTsvToolbar.setIndicatorMatchWidth(true);
        mVpFragment = (ViewPager) rootView.findViewById(com.zhiyicx.baseproject.R.id.vp_fragment);
        mVpFragment.setOffscreenPageLimit(getOffsetPage());
        tsViewPagerAdapter = new TSViewPagerAdapter(getChildFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mVpFragment.setAdapter(tsViewPagerAdapter);
        mTsvToolbar.setAdjustMode(isAdjustMode());
        mTsvToolbar.initTabView(mVpFragment, initTitles());
        mTsvToolbar.setLeftClickListener(this, () -> setLeftClick());
        mTsvToolbar.setRightClickListener(this, () -> {
            Intent typeIntent = new Intent(getActivity(), CircleTyepsActivity.class);
            startActivity(typeIntent);
        });
    }

    @Override
    protected void initData() {
        mPresenter.getCategroiesList(0, 0);
    }


    /**
     * 认证检查回调
     *
     * @param userCertificationInfo
     */
    @Override
    public void setUserCertificationInfo(UserCertificationInfo userCertificationInfo) {
        mUserCertificationInfo = userCertificationInfo;
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        if (mSystemConfigBean.getCircleGroup() != null && mSystemConfigBean.getCircleGroup()
                .isNeed_verified() && userCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.PASS.value) {
            showCerificationPopWindow();
        } else {
            CreateCircleActivity.startCreateActivity(mActivity);
        }

    }

    /**
     * 认证提示弹窗
     */
    private void showCerificationPopWindow() {

        if (mCertificationAlertPopWindow == null) {
            mCertificationAlertPopWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .item2Str(getString(R.string.certification_personage))
                    .item3Str(getString(R.string.certification_company))
                    .desStr(getString(R.string.circle_publish_hint_certification))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .bottomClickListener(() -> mCertificationAlertPopWindow.hide())
                    .item2ClickListener(() -> {// 个人认证
                        mCertificationAlertPopWindow.hide();
                        if (mUserCertificationInfo != null // 待审核
                                && mUserCertificationInfo.getId() != 0
                                && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
                            Intent intentToDetail = new Intent(getActivity(), CertificationDetailActivity.class);
                            Bundle bundleData = new Bundle();
                            bundleData.putInt(BUNDLE_DETAIL_TYPE, 0);
                            bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
                            intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
                            startActivity(intentToDetail);
                        } else {
                            Intent intent = new Intent(getActivity(), CertificationInputActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BUNDLE_TYPE, 0);
                            intent.putExtra(BUNDLE_CERTIFICATION_TYPE, bundle);
                            startActivity(intent);
                        }
                    })
                    .item3ClickListener(() -> {// 企业认证
                        mCertificationAlertPopWindow.hide();
                        if (mUserCertificationInfo != null // 待审核
                                && mUserCertificationInfo.getId() != 0
                                && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {

                            Intent intentToDetail = new Intent(getActivity(), CertificationDetailActivity.class);
                            Bundle bundleData = new Bundle();
                            bundleData.putInt(BUNDLE_DETAIL_TYPE, 1);
                            bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
                            intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
                            startActivity(intentToDetail);
                        } else {
                            Intent intent = new Intent(getActivity(), CertificationInputActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BUNDLE_TYPE, 1);
                            intent.putExtra(BUNDLE_CERTIFICATION_TYPE, bundle);
                            startActivity(intent);
                        }
                    })
                    .build();
        }
        mCertificationAlertPopWindow.show();

    }
}
