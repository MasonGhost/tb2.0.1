package com.zhiyicx.zhibolibrary.ui.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.presenter.LivePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.SearchActivity;
import com.zhiyicx.zhibolibrary.ui.adapter.AdapterViewPager;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.view.LiveView;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public abstract class LiveFragment extends ZBLBaseFragment implements LiveView, ViewPager.OnPageChangeListener, View.OnClickListener {

    protected ViewPager mViewPager;

    TextView mHotTV;

    TextView mSubscribeTV;

    TextView mNewTV;

    ImageView mFilterTV;

    AutoRelativeLayout mToolBar;


    AutoLinearLayout mCotainerRL;

    LinearLayout mMaskView;


    protected List<ZBLBaseFragment> mFragmentList;
    private List<LiveItemFragment> mLiveFragmentList;
    private AdapterViewPager mAdapter;
    private LivePresenter mPresenter;
    protected boolean isShow;


    @Override
    protected void initData() {
        findView();
        mPresenter = new LivePresenter(this);//创建presenter负责逻辑
        initViewPager();
        initMaskView();
        mPresenter.initFilter(mCotainerRL);
    }

    private void findView() {
        mHotTV = (TextView) mRootView.findViewById(R.id.tv_live_hot_tab);
        mHotTV.setOnClickListener(this);
        mSubscribeTV = (TextView) mRootView.findViewById(R.id.tv_live_subscribe_tab);
        mSubscribeTV.setOnClickListener(this);
        mNewTV = (TextView) mRootView.findViewById(R.id.tv_live_news_tab);
        mNewTV.setOnClickListener(this);
        mRootView.findViewById(R.id.tv_live_search).setOnClickListener(this);
        mFilterTV = (ImageView) mRootView.findViewById(R.id.tv_live_filter);
        mFilterTV.setOnClickListener(this);
        mToolBar = (AutoRelativeLayout) mRootView.findViewById(R.id.toolbar);
        mCotainerRL = (AutoLinearLayout) mRootView.findViewById(R.id.rl_live_filter_container);
        mMaskView = (LinearLayout) mRootView.findViewById(R.id.ll_live_mask);

    }

    private void initMaskView() {
        mMaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFilter();
            }
        });
        mCotainerRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //初始化viewpager
    private void initViewPager() {
        //设置缓存的个数
        mViewPager.setOffscreenPageLimit(3);
        mAdapter = new AdapterViewPager(getFragmentManager());
        mFragmentList = getFragements();
        mLiveFragmentList = new ArrayList<>();//赋值给新集合做其他操作
        for (ZBLBaseFragment f : mFragmentList) {
            mLiveFragmentList.add((LiveItemFragment) f);
        }
        mAdapter.bindData(mFragmentList);//将List设置给adapter
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);//设置滚动监听
    }

    protected abstract List<ZBLBaseFragment> getFragements();


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_live_hot_tab) {
            //加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(1);
        }
        else if (v.getId() == R.id.tv_live_subscribe_tab) {
            //加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(0);
        }
        else if (v.getId() == R.id.tv_live_news_tab) {
            //加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(2);
        }
        else if (v.getId() == R.id.tv_live_search) {
            startActivity(new Intent(UiUtils.getContext(), SearchActivity.class));//跳转到搜索页面
            getActivity().overridePendingTransition(R.anim.vote_slide_in_from_left, R.anim.animate_null);//动画
        }
        else if (v.getId() == R.id.tv_live_filter) {
            if (!mLiveFragmentList.get(mViewPager.getCurrentItem()).isFilter()) {
                showFilter();//显示筛选页面
                setFilterSatus(isShow);//切换筛选按钮颜色
            }
            else {
                setFilterSatus(false);
                showMessage("取消筛选");
                mLiveFragmentList.get(mViewPager.getCurrentItem())//取消过滤
                        .setIsFilter(false);
                mLiveFragmentList.get(mViewPager.getCurrentItem())//刷新列表
                        .RefreshList();
            }


        }
    }


    /**
     * 设置tab的颜色
     *
     * @param position
     */
    public void setTabColor(int position) {
        mSubscribeTV.setTextColor(position == 0 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("white"));
        mHotTV.setTextColor(position == 1 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("white"));
        mNewTV.setTextColor(position == 2 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("white"));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTabColor(position);
        mLiveFragmentList.get(position).setData();//加载当前页，并跳转当当前页
        setFilterSatus(mLiveFragmentList.get(position).isFilter());
        if (position == 0) {
            mFilterTV.setVisibility(View.INVISIBLE);
            hideFilter();
        }
        else
            mFilterTV.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }


    @Override
    public void showFilter() {
        if (!isShow) {//判断打开或者关闭
            isShow = true;
            mCotainerRL.setVisibility(View.VISIBLE);
            mMaskView.setVisibility(View.VISIBLE);
            showAnimation();
        }
        else {
            hideFilter();
        }
    }

    @Override
    public void hideFilter() {
        isShow = false;
        setFilterSatus(false);//是筛选按钮变成灰色
        mCotainerRL.setVisibility(View.GONE);
        mMaskView.setVisibility(View.GONE);
        hideAnimation();

    }

    /**
     * 隐藏筛选页面不带动画
     */
    @Override
    public void hideFilterNotAni() {
        isShow = false;
        setFilterSatus(false);//是筛选按钮变成灰色
        mCotainerRL.setVisibility(View.GONE);
        mMaskView.setVisibility(View.GONE);
    }

    @Override
    public void showAnimation() {//显示动画
        mCotainerRL.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
        mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_dialog_show));
    }

    @Override
    public void hideAnimation() {//隐藏动画
        mCotainerRL.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
        mMaskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_dialog_disappear));
    }

    public void startFilter(Map<String, Object> map) {
        mLiveFragmentList.get(mViewPager.getCurrentItem())
                .setIsFilter(true);
        mLiveFragmentList.get(mViewPager.getCurrentItem())
                .setFilterValue(map);

        mLiveFragmentList.get(mViewPager.getCurrentItem())
                .RefreshList();

    }


    @Override
    public void killMyself() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
