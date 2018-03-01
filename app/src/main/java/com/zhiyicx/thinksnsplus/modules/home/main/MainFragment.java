package com.zhiyicx.thinksnsplus.modules.home.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.TBDynamicFragment;
import com.zhiyicx.thinksnsplus.modules.tb.search.SearchMechanismUserActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @Describe 主页 MainFragment
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MainFragment extends TSViewPagerFragment implements DynamicFragment.OnCommentClickListener {
    // 关注动态列表位置，如果更新了，记得修改这儿
    public static final int PAGER_FOLLOW_DYNAMIC_LIST_POSITION = 0;
    @BindView(R.id.v_status_bar_placeholder)
    View mStatusBarPlaceholder;
    @BindView(R.id.v_shadow)
    View mVShadow;
//    @BindView(R.id.iv_search)
//    ImageView mIvSearch;

    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

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
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getOffsetPage() {
        return 2;
    }

    @Override
    protected void initView(View rootView) {
        // 需要在 initview 之前，应为在 initview 中使用了 dagger 注入的数据
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        super.initView(rootView);
        initToolBar();
//        mIvSearch.setVisibility(View.VISIBLE);
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DeviceUtils
                .getStatuBarHeight(getContext()));
        mStatusBarPlaceholder.setLayoutParams(layoutParams);
        // 适配非6.0以上、非魅族系统、非小米系统状态栏
        if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0) {
            mStatusBarPlaceholder.setBackgroundResource(R.color.themeColor);
        }
        //不需要返回键
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setRightImg(R.mipmap.ico_search_normal);
        mTsvToolbar.setRightClickListener(this, () -> {
            startActivity(new Intent(mActivity, SearchMechanismUserActivity.class));
        });
    }

    @Override
    protected void initData() {

        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 游客处理
                if (!TouristConfig.FOLLOW_CAN_LOOK && position == mVpFragment.getChildCount() - 1 && !mIAuthRepository.isLogin()) {
                    showLoginPop();
                    // 转回热门
                    mVpFragment.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    ((DynamicContract.View) mFragmentList.get(mVpFragment.getCurrentItem())).closeInputView();
                }
            }
        });

        // 启动 app，如果本地没有最新数据，应跳到“热门”页面 关联 github  #113  #366
        if (mDynamicBeanGreenDao.getNewestDynamicList(System.currentTimeMillis()).size() == 0) {
            mVpFragment.setCurrentItem(1);
        }

    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.the_last)
                , getString(R.string.hot)
                , getString(R.string.follow));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this));
            mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS, this));
            // 游客处理
            if (TouristConfig.FOLLOW_CAN_LOOK || mIAuthRepository.isLogin()) {
                mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS, this));
            } else {
                // 用于viewpager 占位
                mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_EMPTY, this));
            }
        }
        return mFragmentList;
    }


    @Override
    public void onButtonMenuShow(boolean isShow) {
        mVShadow.setVisibility(isShow ? View.GONE : View.VISIBLE);
        if (mOnCommentClickListener != null) {
            mOnCommentClickListener.onButtonMenuShow(isShow);
        }
    }

    /**
     * viewpager页面切换公开方法
     */
    public void setPagerSelection(int position) {
        mVpFragment.setCurrentItem(position, true);
    }

    /**
     * 刷新当前页
     */
    public void refreshCurrentPage() {
        ((ITSListView) mFragmentList.get(mVpFragment.getCurrentItem())).startRefrsh();
    }

}
