package com.zhiyicx.thinksnsplus.modules.home.message.container;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist.NotificationFragment;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @author Catherine
 * @describe 消息页面
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class MessageContainerFragment extends TSViewPagerFragment{

    private static final int DEFAULT_TAB_UNSELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color.normal_for_assist_text;// 缺省的tab未选择文字
    private static final int DEFAULT_TAB_SELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color.important_for_content;// 缺省的tab被选择文字
    private static final int DEFAULT_TAB_TEXTSIZE = com.zhiyicx.baseproject.R.integer.tab_text_size;// 缺省的tab文字大小
    private static final int DEFAULT_TAB_MARGIN = com.zhiyicx.baseproject.R.integer.tab_margin;// 缺省的tab左右padding
    private static final int DEFAULT_TAB_PADDING = com.zhiyicx.baseproject.R.integer.tab_padding;// 缺省的tab的线和文字的边缘距离
    private static final int DEFAULT_TAB_LINE_COLOR = com.zhiyicx.baseproject.R.color.themeColor;// 缺省的tab的线的颜色
    private static final int DEFAULT_TAB_LINE_HEGIHT = com.zhiyicx.baseproject.R.integer.line_height;// 缺省的tab的线的高度

    @BindView(R.id.v_status_bar_placeholder)
    View mStatusBarPlaceholder;

    List<Fragment> mFragments;

    public MessageContainerFragment instance(){
        return new MessageContainerFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_main_viewpager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolBar();
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.message), getString(R.string.notification));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragments == null){
            mFragments = new ArrayList<>();
        }
        mFragments.add(MessageFragment.newInstance());
        mFragments.add(new NotificationFragment().instance());
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
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DeviceUtils.getStatuBarHeight(getContext()));
        mStatusBarPlaceholder.setLayoutParams(layoutParams);
        if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0) { // 适配非6.0以上、非魅族系统、非小米系统状态栏
            mStatusBarPlaceholder.setBackgroundResource(R.color.themeColor);
        }
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
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, DEFAULT_TAB_UNSELECTED_TEXTCOLOR));
                int leftRightPadding = UIUtil.dip2px(context, getContext().getResources().getInteger(DEFAULT_TAB_MARGIN));
                simplePagerTitleView.setPadding(leftRightPadding, 0, leftRightPadding, 0);
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, DEFAULT_TAB_SELECTED_TEXTCOLOR));
                simplePagerTitleView.setText(mStringList.get(index));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(DEFAULT_TAB_TEXTSIZE));
                simplePagerTitleView.setOnClickListener(v -> {
                    mVpFragment.setCurrentItem(index);
                    badgePagerTitleView.setBadgeView(null); // cancel badge when click tab
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                // setup badge
                TextView badgeTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
//                badgeTextView.setText("" + (index + 1));
                badgePagerTitleView.setBadgeView(badgeTextView);

                // set badge position
                if (index == 0) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                } else if (index == 1) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 10)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                }

                // don't cancel badge when tab selected
                badgePagerTitleView.setAutoCancelBadge(false);
                return badgePagerTitleView;
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
