package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhiyicx.thinksnsplus.widget.coordinatorlayout.AppBarLayoutOverScrollViewBehavior;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/11/21/9:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailFragment extends TSListFragment<CircleDetailContract.Presenter, CirclePostListBean>
        implements CircleDetailContract.View {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.circle_title_layout)
    RelativeLayout mTitleContainerParent;
    @BindView(R.id.circle_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;

    private ActionBarDrawerToggle mToggle;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_circle_detail;
    }

    @Override
    protected boolean setUseStatusView() {
        return super.setUseStatusView();
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    public long getCircleId() {
        return 2;
    }

    @Override
    public void allDataReady(CircleZipBean circleZipBean) {
        closeLoadingView();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem());
        return adapter;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolBar();
        mDrawer.setClipToPadding(false);
        mDrawer.setClipChildren(false);
        mToggle = new ActionBarDrawerToggle(getActivity(), mDrawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener((progress, isRelease) -> {
            mIvRefresh.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mIvRefresh.getDrawable()).start();
        });
    }

    private void initToolBar() {
        if (!setUseStatusView()) {
            // toolBar 设置状态栏高度的 marginTop
            int marginTop = DeviceUtils.getStatuBarHeight(getContext()) +
                    getResources().getDimensionPixelSize(R.dimen.divider_line);
            int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height) + marginTop;
            CollapsingToolbarLayout.LayoutParams layoutParams = new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mTitleContainerParent.setLayoutParams(layoutParams);
        }
    }
}
