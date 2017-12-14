package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.ChannelActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
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
public class InfoContainerFragment extends TSFragment<InfoMainContract.InfoContainerPresenter>
        implements InfoMainContract.InfoContainerView {

    @BindView(R.id.fragment_infocontainer_indoctor)
    MagicIndicator mFragmentInfocontainerIndoctor;
    @BindView(R.id.fragment_infocontainer_change)
    ImageView mFragmentInfocontainerChange;
    @BindView(R.id.fragment_infocontainer_content)
    ViewPager mFragmentInfocontainerContent;

    public static final String SUBSCRIBE_EXTRA = "mycates";
    protected static final int DEFAULT_OFFSET_PAGE = 3;
    public static final String RECOMMEND_INFO = "-1";
    public static final int REQUEST_CODE = 0;

    private UserCertificationInfo mUserCertificationInfo;

    // 定义默认样式值
    private static final int DEFAULT_TAB_UNSELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color
            .normal_for_assist_text;// 缺省的tab未选择文字

    // 缺省的tab被选择文字
    private static final int DEFAULT_TAB_SELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color
            .important_for_content;

    // 缺省的tab文字大小
    private static final int DEFAULT_TAB_TEXTSIZE = com.zhiyicx.baseproject.R.integer
            .tab_text_size;

    // 缺省的tab之间的空白间距
    private static final int DEFAULT_TAB_MARGIN = com.zhiyicx.baseproject.R.integer.tab_margin;//

    // 缺省的tab的线和文字的边缘距离
    private static final int DEFAULT_TAB_PADDING = com.zhiyicx.baseproject.R.integer.tab_padding;

    // 缺省的tab的线的颜色
    private static final int DEFAULT_TAB_LINE_COLOR = com.zhiyicx.baseproject.R.color.themeColor;

    // 缺省的tab的线的高度
    private static final int DEFAULT_TAB_LINE_HEGIHT = com.zhiyicx.baseproject.R.integer
            .no_line_height;

    private List<String> mTitle;
    private List<Fragment> mFragments;
    private MyAdapter mMyAdapter;
    private InfoTypeBean mInfoTypeBean;
    private CommonNavigator mCommonNavigator;

    private ActionPopupWindow mCertificationAlertPopWindow; // 提示需要认证的
    private ActionPopupWindow mPayAlertPopWindow; // 提示需要付钱的

    private SystemConfigBean.NewsConfig mPublishInfoConfig;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_infocontainer;
    }

    @Override
    protected void initView(View rootView) {
        mFragmentInfocontainerContent.setOffscreenPageLimit(DEFAULT_OFFSET_PAGE);
        mMyAdapter = new MyAdapter(getFragmentManager());
        initMagicIndicator(initTitles());
        initFragments();
        mFragmentInfocontainerContent.setAdapter(mMyAdapter);
        mFragmentInfocontainerContent.setOffscreenPageLimit(1);
        initPopWindow();
    }

    @Override
    public void setUserCertificationInfo(UserCertificationInfo userCertificationInfo) {
        mUserCertificationInfo = userCertificationInfo;
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        mPublishInfoConfig = mSystemConfigBean.getNewsContribute();
        if (userCertificationInfo.getStatus() == 1 || !mPublishInfoConfig.hasVerified()) {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mTitle.clear();
            mFragments.clear();
            mInfoTypeBean = data.getBundleExtra(SUBSCRIBE_EXTRA).getParcelable(SUBSCRIBE_EXTRA);

            Observable.from(mInfoTypeBean.getMy_cates())
                    .subscribe(myCatesBean -> {
                        mTitle.add(myCatesBean.getName());
                        mFragments.add(InfoListFragment.newInstance(myCatesBean.getId() + ""));
                    });
            mMyAdapter.notifyDataSetChanged();
            mCommonNavigator.notifyDataSetChanged();
        }

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

    @OnClick(R.id.fragment_infocontainer_change)
    public void onClick() {
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
                mFragments.add(InfoListFragment.newInstance(myCatesBean.getId() + ""));
            }
        }
        mMyAdapter.notifyDataSetChanged();
        initMagicIndicator(mTitle);
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
                    .item2ClickListener(() -> {// 个人认证
                        mCertificationAlertPopWindow.hide();
                        if (mUserCertificationInfo != null // 待审核
                                && mUserCertificationInfo.getId() != 0
                                && mUserCertificationInfo.getStatus() != 2) {
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
                                && mUserCertificationInfo.getStatus() != 2) {

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

    protected List<String> initTitles() {
        if (mTitle == null) {
            mTitle = new ArrayList<>();
            mTitle.add(getString(R.string.info_recommend));
        }
        return mTitle;
    }

    protected List<Fragment> initFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(InfoListFragment.newInstance(RECOMMEND_INFO));
        }
        return mFragments;
    }

    private void initMagicIndicator(final List<String> mStringList) {
        mFragmentInfocontainerIndoctor.setBackgroundColor(Color.WHITE);
        mCommonNavigator = new CommonNavigator(getActivity());
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mStringList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView
                        (context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context,
                        DEFAULT_TAB_UNSELECTED_TEXTCOLOR));

                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context,
                        DEFAULT_TAB_SELECTED_TEXTCOLOR));

                simplePagerTitleView.setText(mStringList.get(index));

                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources
                        ().getInteger(DEFAULT_TAB_TEXTSIZE));

                simplePagerTitleView.setOnClickListener(v -> mFragmentInfocontainerContent.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);// 占满
                linePagerIndicator.setXOffset(UIUtil.dip2px(context, context.getResources()
                        .getInteger(DEFAULT_TAB_PADDING)));// 每个item边缘到指示器的边缘距离
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, context.getResources()
                        .getInteger(DEFAULT_TAB_LINE_HEGIHT)));
                linePagerIndicator.setColors(ContextCompat.getColor(context,
                        DEFAULT_TAB_LINE_COLOR));
                return linePagerIndicator;
            }
        });
        mFragmentInfocontainerIndoctor.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mFragmentInfocontainerIndoctor, mFragmentInfocontainerContent);

    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}
