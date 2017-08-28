package com.zhiyicx.thinksnsplus.modules.q_a.mine.container;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyQuestionFragment extends TSViewPagerFragment{

    private List<Fragment> mFragments;

    public MyQuestionFragment instance(){
        return new MyQuestionFragment();
    }

    @Override
    public void setPresenter(Object presenter) {
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.qa_mine_question),
                getString(R.string.qa_mine_answer),
                getString(R.string.qa_mine_follow));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.add(new MyPublishQuestionContainerFragment());
        mFragments.add(new MyAnswerContainerFragment());
        mFragments.add(new MyFollowContainerFragment());
        return mFragments;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.initTabView(mVpFragment, initTitles(), R.drawable.shape_question_tool_bg, getCommonNavigatorAdapter(initTitles()));
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
                clipPagerTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(v -> mVpFragment.setCurrentItem(index));
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = context.getResources().getDimension(R.dimen.qa_top_select_height);
                float borderWidth = UIUtil.dip2px(context, 1);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(lineHeight / 2);
                indicator.setYOffset(borderWidth);
                indicator.setColors(ContextCompat.getColor(getContext(), R.color.themeColor));
                return indicator;
            }
        };
    }
}
