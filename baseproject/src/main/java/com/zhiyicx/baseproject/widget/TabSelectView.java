package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zhiyicx.baseproject.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 和viewpager关联的tab选项卡，集成在toolbar中；
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class TabSelectView extends FrameLayout {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private List<String> mStringList;// tab列表的文字
    private Context mContext;
    private View divider;

    public TabSelectView(Context context) {
        super(context);
        initView(context);
    }

    public TabSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_for_viewpager, this);
        mMagicIndicator = (MagicIndicator) findViewById(R.id.mg_indicator);
        divider = findViewById(R.id.divider);
        mContext = context;
    }

    public void initTabView(ViewPager viewPager, List<String> stringList) {
        this.mViewPager = viewPager;
        this.mStringList = stringList;
        if (mStringList == null) {
            mStringList = new ArrayList<>();
        }
        initMagicIndicator(mContext);
    }

    /**
     * 是否需要展示toolbar分割线
     *
     * @param showDivider
     */
    public void showDivider(boolean showDivider) {
        divider.setVisibility(showDivider ? GONE : VISIBLE);
    }

    private void initMagicIndicator(Context context) {
        mMagicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mStringList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.normal_for_dynamic_list_content));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.important_for_content));
                simplePagerTitleView.setText(mStringList.get(index));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);// 适应文字长度
                linePagerIndicator.setLineHeight(5);
                linePagerIndicator.setColors(ContextCompat.getColor(context, R.color.themeColor));
                return linePagerIndicator;
            }
        });
        commonNavigator.setRightPadding(25);
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }


}
