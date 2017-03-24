package com.zhiyicx.thinksnsplus.modules.home.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Describe 主页MainFragment
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MainFragment extends TSViewPagerFragment implements DynamicFragment.OnCommentClickListener {

    @BindView(R.id.v_status_bar_placeholder)
    View mStatusBarPlaceholder;
    @BindView(R.id.v_shadow)
    View mVShadow;
    List<Fragment> fragments = new ArrayList<>();

    public void setOnImageClickListener(DynamicFragment.OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    DynamicFragment.OnCommentClickListener mOnCommentClickListener;

    public static MainFragment newInstance(DynamicFragment.OnCommentClickListener l) {
        MainFragment fragment = new MainFragment();
        fragment.setOnImageClickListener(l);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_main_viewpager;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolBar();

    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DeviceUtils.getStatuBarHeight(getContext()));
        mStatusBarPlaceholder.setLayoutParams(layoutParams);
        mTsvToolbar.setLeftImg(0);//不需要返回键
    }

    @Override
    protected void initData() {
        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    ((DynamicContract.View) fragments.get(mVpFragment.getCurrentItem())).closeInputView();
                }
            }
        });
    }

    @Override
    protected List<String> initTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.follow));
        titles.add(getString(R.string.hot));
        titles.add(getString(R.string.the_last));
        return titles;
    }

    @Override
    protected List<Fragment> initFragments() {
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS, this));
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS, this));
        fragments.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this));
        return fragments;
    }


    @Override
    public void onButtonMenuShow(boolean isShow) {
        if (!isShow) {
            mVShadow.setVisibility(View.VISIBLE);
        } else {
            mVShadow.setVisibility(View.GONE);
        }
        if (mOnCommentClickListener != null) {
            mOnCommentClickListener.onButtonMenuShow(isShow);
        }
    }

}
