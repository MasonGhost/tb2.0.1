package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */
public class HomeFragment extends TSFragment {
    public static final int PAGE_NUMS = 4; // 页数

    public static final int PAGE_HOME = 0; // 对应在 viewpager 中的位置
    public static final int PAGE_FIND = 1;
    public static final int PAGE_MESSAGE = 2;
    public static final int PAGE_MINE = 3;

    @BindView(R.id.iv_home)
    ImageView mIvHome;
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.ll_home)
    LinearLayout mLlHome;
    @BindView(R.id.iv_find)
    ImageView mIvFind;
    @BindView(R.id.tv_find)
    TextView mTvFind;
    @BindView(R.id.ll_find)
    LinearLayout mLlFind;
    @BindView(R.id.fl_add)
    FrameLayout mFlAdd;
    @BindView(R.id.iv_message)
    ImageView mIvMessage;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.ll_message)
    LinearLayout mLlMessage;
    @BindView(R.id.iv_mine)
    ImageView mIvMine;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    @BindView(R.id.ll_mine)
    LinearLayout mLlMine;
    @BindView(R.id.vp_home)
    NoPullViewPager mVpHome;
    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        initViewPager();

    }


    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_home;
    }

    @OnClick({R.id.ll_home, R.id.ll_find})
    public void onClick(final View view) {
        RxView.clicks(view)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        switch (view.getId()) {
                            // 点击主页
                            case R.id.ll_home:
                                mVpHome.setCurrentItem(PAGE_HOME);
                                break;
                            // 点击发现
                            case R.id.ll_find:
                                mVpHome.setCurrentItem(PAGE_FIND);
                                break;
                            // 点击发现
                            case R.id.ll_message:
                                mVpHome.setCurrentItem(PAGE_MESSAGE);
                                break;
                            // 点击发现
                            case R.id.ll_mine:
                                mVpHome.setCurrentItem(PAGE_MINE);
                                break;
                            default:
                        }
                    }
                });

    }

    /**
     * 初始化 viewpager
     */

    private void initViewPager() {
        //设置缓存的个数
        mVpHome.setOffscreenPageLimit(4);
//        mAdapter = new AdapterViewPager(getSupportFragmentManager());
//        mFragmentList = mPresenter.getFragments();
//        mAdapter.bindData(mFragmentList);//将List设置给adapter
//        mVpHome.setAdapter(mAdapter);


    }

    /**
     * 设置监听
     */
    private void initListener() {
        //设置滚动监听
        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeNavigationButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 改变导航栏按钮的状态
     *
     * @param position 当前 viewpager 的位置
     */
    private void changeNavigationButton(int position) {
        int checkedColor = ContextCompat.getColor(getContext(), R.color.themeColor);
        int unckeckedColor = ContextCompat.getColor(getContext(), R.color.home_bottom_navigate_text_normal);
        mIvHome.setImageResource(position == PAGE_HOME ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
        mTvHome.setTextColor(position == PAGE_HOME ? checkedColor : unckeckedColor);
        mIvFind.setImageResource(position == PAGE_FIND ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
        mTvFind.setTextColor(position == PAGE_FIND ? checkedColor : unckeckedColor);
        mIvMessage.setImageResource(position == PAGE_MESSAGE ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
        mTvMessage.setTextColor(position == PAGE_MESSAGE ? checkedColor : unckeckedColor);
        mIvMine.setImageResource(position == PAGE_MINE ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
        mTvMine.setTextColor(position == PAGE_MINE ? checkedColor : unckeckedColor);
    }


}
