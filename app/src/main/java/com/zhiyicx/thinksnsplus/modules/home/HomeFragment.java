package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.iv_find)
    ImageView mIvFind;
    @BindView(R.id.tv_find)
    TextView mTvFind;
    @BindView(R.id.iv_message)
    ImageView mIvMessage;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.iv_mine)
    ImageView mIvMine;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    @BindView(R.id.vp_home)
    NoPullViewPager mVpHome;

    private TSViewPagerAdapter mHomePager;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 不需要 toolbar
     *
     * @return
     */
    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected void initView(View rootView) {
        initViewPager();
        initListener();
        changeNavigationButton(PAGE_HOME);
        mVpHome.setCurrentItem(PAGE_HOME, false);
    }


    @Override
    protected void initData() {
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_home;
    }

    @OnClick({R.id.ll_home, R.id.ll_find, R.id.fl_add, R.id.ll_message, R.id.ll_mine})
    public void onClick(final View view) {
        switch (view.getId()) {
            // 点击主页
            case R.id.ll_home:
                mVpHome.setCurrentItem(PAGE_HOME, false);
                break;
            // 点击发现
            case R.id.ll_find:
                mVpHome.setCurrentItem(PAGE_FIND, false);
                break;
            // 点击增加
            case R.id.fl_add:
                //// TODO: 2017/1/5  添加动态
                break;
            // 点击消息
            case R.id.ll_message:
                mVpHome.setCurrentItem(PAGE_MESSAGE, false);
                break;
            // 点击我的
            case R.id.ll_mine:
                mVpHome.setCurrentItem(PAGE_MINE, false);
                break;
            default:
        }

    }

    /**
     * 初始化 viewpager
     */

    private void initViewPager() {
        //设置缓存的个数
        mVpHome.setOffscreenPageLimit(PAGE_NUMS);
        mHomePager = new TSViewPagerAdapter(getActivity().getSupportFragmentManager());
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(MainFragment.newInstance());
        mFragmentList.add(FindFragment.newInstance());
        mFragmentList.add(MessageFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());
        mHomePager.bindData(mFragmentList);//将List设置给adapter
        mVpHome.setAdapter(mHomePager);
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
        mIvHome.setImageResource(position == PAGE_HOME ? R.mipmap.common_ico_bottom_home_high : R.mipmap.common_ico_bottom_home_normal);
        mTvHome.setTextColor(position == PAGE_HOME ? checkedColor : unckeckedColor);
        mIvFind.setImageResource(position == PAGE_FIND ? R.mipmap.common_ico_bottom_discover_high : R.mipmap.common_ico_bottom_discover_normal);
        mTvFind.setTextColor(position == PAGE_FIND ? checkedColor : unckeckedColor);
        mIvMessage.setImageResource(position == PAGE_MESSAGE ? R.mipmap.common_ico_bottom_message_high : R.mipmap.common_ico_bottom_message_normal);
        mTvMessage.setTextColor(position == PAGE_MESSAGE ? checkedColor : unckeckedColor);
        mIvMine.setImageResource(position == PAGE_MINE ? R.mipmap.common_ico_bottom_me_high : R.mipmap.common_ico_bottom_me_normal);
        mTvMine.setTextColor(position == PAGE_MINE ? checkedColor : unckeckedColor);
    }


}
