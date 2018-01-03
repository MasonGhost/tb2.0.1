package com.zhiyicx.thinksnsplus.modules.circle.mine.container;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 我的圈子
 * @Author Jungle68
 * @Date 2017/11/14
 * @Contact master.jungle68@gmail.com
 */
public class MyCircleContainerFragment extends TSViewPagerFragment {

    private static final int DEFAULT_OFFSET_PAGE = 1;

    @BindView(R.id.mg_indicator)
    MagicIndicator mMgIndicator;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.vp_fragment)
    ViewPager mVpFragment;

    public static MyCircleContainerFragment instance() {
        return new MyCircleContainerFragment();
    }


    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.group),
                getString(R.string.circle_post));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(new MyCircleContentContainerFragment());
            mFragmentList.add(new MyCirclePostContentContainerFragment());
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_circle_container;
    }

    @Override
    protected void initViewPager(View rootView) {
        initMagicIndicator();
        mVpFragment = (ViewPager) rootView.findViewById(R.id.vp_fragment);
        mVpFragment.setOffscreenPageLimit(DEFAULT_OFFSET_PAGE);
        tsViewPagerAdapter = new TSViewPagerAdapter(getChildFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mVpFragment.setAdapter(tsViewPagerAdapter);
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(bindToLifecycle())
                .subscribe(aVoid -> getActivity().finish());

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
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mStringList.get(index));
                clipPagerTitleView.setTextSize(getResources().getDimensionPixelSize(R.dimen.size_sub_title));
                clipPagerTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(v -> mVpFragment.setCurrentItem(index));
                clipPagerTitleView.setPadding(UIUtil.dip2px(context, 18.0D), 0, UIUtil.dip2px(context, 18.0D), 0);
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = context.getResources().getDimension(R.dimen.qa_top_select_height);
                float borderWidth = UIUtil.dip2px(context, 0);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(2);
                indicator.setYOffset(borderWidth);
                indicator.setColors(ContextCompat.getColor(getContext(), R.color.themeColor));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return super.getTitleWeight(context, index);
            }
        };
    }

    private void initMagicIndicator() {
        mMgIndicator.setBackgroundResource(R.drawable.shape_question_tool_bg);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(getCommonNavigatorAdapter(initTitles()));
        mMgIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.shape_question_tool_diver));
        ViewPagerHelper.bind(mMgIndicator, mVpFragment);
    }
}
