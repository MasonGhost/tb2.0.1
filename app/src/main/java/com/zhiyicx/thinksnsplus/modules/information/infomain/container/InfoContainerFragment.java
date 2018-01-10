package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.TabSelectView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.ChannelActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;

import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_CERTIFICATION_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯的分类
 */
public class InfoContainerFragment extends TSViewPagerFragment<InfoMainContract.InfoContainerPresenter>
        implements InfoMainContract.InfoContainerView {

    private List<String> mTitle;

    public static final String SUBSCRIBE_EXTRA = "mycates";
    public static final String RECOMMEND_INFO = "-1";
    public static final int REQUEST_CODE = 0;

    private UserCertificationInfo mUserCertificationInfo;

    // 提示需要认证的
    private ActionPopupWindow mCertificationAlertPopWindow;

    // 提示需要付钱的
    private ActionPopupWindow mPayAlertPopWindow;

    private SystemConfigBean.NewsConfig mPublishInfoConfig;
    private InfoTypeBean mInfoTypeBean;

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.information);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_news_contribute;
    }

    @Override
    protected void setRightClick() {
        // 发布提示 1、首先需要认证 2、需要付费
        if (mPresenter.handleTouristControl()) {
            return;
        }
        mPresenter.checkCertification();
    }

    @Override
    protected void musicWindowsStatus(boolean isShow) {
        super.musicWindowsStatus(isShow);
    }

    @Override
    protected void initData() {
        mPresenter.getInfoType();
        initPopWindow();
    }

    @Override
    public void setUserCertificationInfo(UserCertificationInfo userCertificationInfo) {
        mUserCertificationInfo = userCertificationInfo;
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        mPublishInfoConfig = mSystemConfigBean.getNewsContribute();
        if (userCertificationInfo.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value || !mPublishInfoConfig.hasVerified()) {
            if (mPresenter.isNeedPayTip() && (mPublishInfoConfig != null
                    && mPublishInfoConfig.hasPay())) {
                mPayAlertPopWindow.show();
            } else {
                startActivity(new Intent(getActivity(), PublishInfoActivity.class));
            }
        } else {
            mCertificationAlertPopWindow.show();
        }
    }

    @Override
    protected void initViewPager(View rootView) {
        mTsvToolbar = (TabSelectView) rootView.findViewById(com.zhiyicx.baseproject.R.id.tsv_toolbar);
        mTsvToolbar.setRightImg(R.mipmap.sec_nav_arrow, R.color.white);
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setDefaultTabLinehegiht(R.integer.no_line_height);
        mTsvToolbar.setDefaultTabLeftMargin(com.zhiyicx.baseproject.R.integer.tab_margin_10);
        mTsvToolbar.setDefaultTabRightMargin(com.zhiyicx.baseproject.R.integer.tab_margin_10);
        mTsvToolbar.showDivider(false);
        mTsvToolbar.setIndicatorMatchWidth(true);
        mVpFragment = (ViewPager) rootView.findViewById(com.zhiyicx.baseproject.R.id.vp_fragment);
        tsViewPagerAdapter = new TSViewPagerAdapter(getChildFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mVpFragment.setAdapter(tsViewPagerAdapter);
        mTsvToolbar.setAdjustMode(isAdjustMode());
        mTsvToolbar.initTabView(mVpFragment, initTitles());
        mTsvToolbar.setLeftClickListener(this, () -> setLeftClick());
        mTsvToolbar.setRightClickListener(this, () -> {
            if (mPresenter.handleTouristControl()) {
                return;
            }
            Intent intent = new Intent(getActivity(), ChannelActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_INFO_TYPE, mInfoTypeBean);
            intent.putExtra(BUNDLE_INFO_TYPE, bundle);
            startActivityForResult(intent, REQUEST_CODE);
            getActivity().overridePendingTransition(R.anim.slide_from_top_enter, R.anim
                    .slide_from_top_quit);
        });
        mVpFragment.setOffscreenPageLimit(getOffsetPage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mTitle.clear();
            mFragmentList.clear();
            mInfoTypeBean = data.getBundleExtra(SUBSCRIBE_EXTRA).getParcelable(SUBSCRIBE_EXTRA);

            Observable.from(mInfoTypeBean.getMy_cates())
                    .subscribe(myCatesBean -> {
                        mTitle.add(myCatesBean.getName());
                        mFragmentList.add(InfoListFragment.newInstance(myCatesBean.getId() + ""));
                    });
            mTsvToolbar.notifyDataSetChanged(mTitle);
            tsViewPagerAdapter.bindData(mFragmentList, mTitle.toArray(new String[]{}));
            mVpFragment.setOffscreenPageLimit(mTitle.size());
        }

    }

    @Override
    protected int setRightLeftImg() {
        return R.mipmap.ico_search;
    }

    @Override
    protected void setRightLeftClick() {
        super.setRightLeftClick();
        if (!TouristConfig.INFO_CAN_SEARCH && mPresenter.handleTouristControl()) {
            return;
        }
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    public void setInfoType(InfoTypeBean infoType) {
        mInfoTypeBean = infoType;
        mInfoTypeBean.getMy_cates().add(0, new InfoTypeCatesBean(-1L, getString(R.string
                .info_recommend), true));
        for (InfoTypeCatesBean myCatesBean : infoType.getMy_cates()) {
            if (mInfoTypeBean.getMy_cates().indexOf(myCatesBean) != 0
                    && !mTitle.contains(myCatesBean.getName())) {
                LogUtils.d(myCatesBean.getName());
                mTitle.add(myCatesBean.getName());
                mFragmentList.add(InfoListFragment.newInstance(myCatesBean.getId() + ""));
            }
        }
        mTsvToolbar.notifyDataSetChanged(mTitle);
        tsViewPagerAdapter.bindData(mFragmentList, mTitle.toArray(new String[]{}));
        mVpFragment.setOffscreenPageLimit(mTitle.size());
    }

    @Override
    public void setPresenter(InfoMainContract.InfoContainerPresenter infoContainerPresenter) {
        mPresenter = infoContainerPresenter;
    }

    private void initPopWindow() {
        if (mCertificationAlertPopWindow == null) {
            mCertificationAlertPopWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .item2Str(getString(R.string.certification_personage))
                    .item3Str(getString(R.string.certification_company))
                    .desStr(getString(R.string.info_publish_hint_certification))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .bottomClickListener(() -> mCertificationAlertPopWindow.hide())
                    .item2ClickListener(() -> {
                        // 个人认证
                        mCertificationAlertPopWindow.hide();
                        // 待审核
                        if (mUserCertificationInfo != null
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
                    .item3ClickListener(() -> {
                        // 企业认证
                        mCertificationAlertPopWindow.hide();
                        // 待审核
                        if (mUserCertificationInfo != null
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
        if (mPayAlertPopWindow == null) {
            mPayAlertPopWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .item6Str(getString(R.string.info_publish_go_to_next))
                    .desStr(String.format(Locale.getDefault(), getString(R.string.info_publish_hint_pay), mPresenter.getGoldName()))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .bottomClickListener(() -> mPayAlertPopWindow.hide())
                    .item6ClickListener(() -> {
                        mPayAlertPopWindow.hide();
                        mPresenter.savePayTip(false);
                        startActivity(new Intent(getActivity(), PublishInfoActivity.class));
                    })
                    .build();
        }
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

        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(InfoListFragment.newInstance(RECOMMEND_INFO));
        }
        return mFragmentList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}
