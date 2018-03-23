package com.zhiyicx.thinksnsplus.modules.home.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListPresenter;
import com.zhiyicx.thinksnsplus.modules.tb.info.TBHomeInfoListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.search.SearchMechanismUserActivity;

import java.util.ArrayList;
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

    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

    /**
     * 顶部分类
     */
    private TextView mChooseBtLeft;
    private TextView mChooseBtRight;

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
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected int setSystemStatusBarCorlorResource() {
        return R.color.themeColor;
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
//        setStatusPlaceholderViewBackgroundColor(ContextCompat.getColor(getContext(),R.color.themeColor));
        //不需要返回键
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setRightImg(R.mipmap.ico_search_normal);
        mTsvToolbar.setBackgroundResource(R.color.themeColor);
        mTsvToolbar.setRightClickListener(this, () -> {
            startActivity(new Intent(mActivity, SearchMechanismUserActivity.class));
        });
        mChooseBtLeft = (TextView) mTsvToolbar.findViewById(R.id.tv_choose_bt_left);
        mChooseBtRight = (TextView) mTsvToolbar.findViewById(R.id.tv_choose_bt_right);
        mChooseBtLeft.setOnClickListener(v -> mVpFragment.setCurrentItem(0));
        mChooseBtRight.setOnClickListener(v -> mVpFragment.setCurrentItem(1));
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
                onPageChanged(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (mFragmentList.get(mVpFragment.getCurrentItem()) instanceof DynamicContract.View) {
                        ((DynamicContract.View) mFragmentList.get(mVpFragment.getCurrentItem())).closeInputView();

                    }
                }
            }
        });

    }

    /**
     * 首页快讯和资讯选中切换
     *
     * @param position
     */
    private void onPageChanged(int position) {

        switch (position) {
            /*
            快讯
             */
            case 0:
                mChooseBtLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                mChooseBtLeft.setBackgroundResource(R.drawable.shape_main_choosed_bt_left);
                mChooseBtRight.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                mChooseBtRight.setBackgroundResource(R.drawable.shape_main_unchoose_bt_right);
                break;
            /*
            资讯
             */
            case 1:
                mChooseBtRight.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                mChooseBtRight.setBackgroundResource(R.drawable.shape_main_choosed_bt_right);
                mChooseBtLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                mChooseBtLeft.setBackgroundResource(R.drawable.shape_main_unchoose_bt_left);
                break;

            default:

        }

    }

    @Override
    protected List<String> initTitles() {
        return null;
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this));
            mFragmentList.add(TBHomeInfoListFragment.newInstance(InfoListPresenter.TB_INFO_TYPE_TOP));

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
