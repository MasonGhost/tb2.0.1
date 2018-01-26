package com.zhiyicx.thinksnsplus.modules.q_a.mine.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_ALL;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_INVITATION;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_OTHER;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.question.MyPublishQuestionFragment.MY_QUESTION_TYPE_REWARD;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyPublishQuestionContainerFragment extends TSViewPagerFragment {

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
            .line_height;

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getResources().getStringArray(R.array.qa_mine_publish_question_title));
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected boolean isAdjustMode() {
        return true;
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
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(setLeftImg());
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.showDivider(false);
        mTsvToolbar.setPadding(getResources().getDimensionPixelOffset(R.dimen.spacing_mid), 0, getResources().getDimensionPixelOffset(R.dimen
                .spacing_mid), 0);
//        mTsvToolbar.initTabView(mVpFragment, initTitles(), getCommonNavigatorAdapter(initTitles()));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Fragment allFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_ALL);
            Fragment invitationFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_INVITATION);
            Fragment rewardFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_REWARD);
            Fragment otherFragment = MyPublishQuestionFragment.getInstance(MY_QUESTION_TYPE_OTHER);
            mFragmentList = Arrays.asList(allFragment, invitationFragment, rewardFragment, otherFragment);
        }
        return mFragmentList;
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

    @Override
    protected void initData() {

    }
}
