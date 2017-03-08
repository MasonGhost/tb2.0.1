package com.zhiyicx.thinksnsplus.modules.information.infomain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.ChannelActivity;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.InfoChannelFragment;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchActivity;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;

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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoContainerFragment extends TSFragment {

    @BindView(R.id.fragment_infocontainer_indoctor)
    MagicIndicator mFragmentInfocontainerIndoctor;
    @BindView(R.id.fragment_infocontainer_change)
    ImageView mFragmentInfocontainerChange;
    @BindView(R.id.fragment_infocontainer_content)
    ViewPager mFragmentInfocontainerContent;

    protected static final int DEFAULT_OFFSET_PAGE = 3;
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

    private InfoChannelFragment mInfoChannelFragment;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_infocontainer;
    }


    @Override
    protected void initView(View rootView) {
        mFragmentInfocontainerContent.setOffscreenPageLimit(DEFAULT_OFFSET_PAGE);
        TSViewPagerAdapter tsViewPagerAdapter = new TSViewPagerAdapter(getFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mFragmentInfocontainerContent.setAdapter(tsViewPagerAdapter);
        initMagicIndicator(initTitles());
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.information);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_search;
    }

    @Override
    protected void setRightClick() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @OnClick(R.id.fragment_infocontainer_change)
    public void onClick() {
        startActivity(new Intent(getActivity(), ChannelActivity.class));
    }

    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        return titles;
    }

    protected List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS));
        fragments.add(InfoListFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW));
        return fragments;
    }

    private void initMagicIndicator(final List<String> mStringList) {
        mFragmentInfocontainerIndoctor.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

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

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFragmentInfocontainerContent.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }


            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                //linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);// 适应文字长度
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

        mFragmentInfocontainerIndoctor.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mFragmentInfocontainerIndoctor, mFragmentInfocontainerContent);
    }
}
