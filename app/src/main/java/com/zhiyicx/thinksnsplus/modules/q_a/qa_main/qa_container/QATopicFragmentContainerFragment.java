package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist.QATopicListFragment;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicFragmentContainerFragment extends TSViewPagerFragment {

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

    private List<Fragment> mFragments;
    public static final String TOPIC_TYPE_FOLLOW = "follow";
    public static final String TOPIC_TYPE_ALL = "all";
    public static final String TOPIC_TYPE_SEARCH = "search";

    public static QATopicFragmentContainerFragment getInstance() {
        return new QATopicFragmentContainerFragment();
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.qa_topic_all), getString(R.string.qa_topic_user_followed));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(QATopicListFragment.newInstance(TOPIC_TYPE_ALL));
            mFragments.add(QATopicListFragment.newInstance(TOPIC_TYPE_FOLLOW));
        }
        return mFragments;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.initTabView(mVpFragment, initTitles(), getCommonNavigatorAdapter(initTitles()));
        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    QATopicListFragment fragment = (QATopicListFragment) mFragments.get(1);
                    if (fragment.handleTouristControl()) {
                        mVpFragment.setCurrentItem(0,false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @NonNull
    private CommonNavigatorAdapter getCommonNavigatorAdapter(final List<String> mStringList) {
        return new CommonNavigatorAdapter() {

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

                simplePagerTitleView.setOnClickListener(v -> mVpFragment.setCurrentItem(index));
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
        };
    }
}
