package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSListFragment<FollowFansListContract.Presenter, UserInfoBean> implements FollowFansListContract.View {
    /**
     * 当前页面是关注页面还是粉丝页面:pageType
     */
    public static final int FANS_FRAGMENT_PAGE = 0;
    public static final int FOLLOW_FRAGMENT_PAGE = 1;

    /**
     * 获取页面类型的key
     */
    public static final String PAGE_TYPE = "page_type";
    /**
     * 获取用户id，决定这是谁的关注粉丝列表
     */
    public static final String PAGE_DATA = "page_data";
    @Inject
    FollowFansListPresenter mFollowFansListPresenter;

    /**
     * 页面类型，由上一个页面决定
     */
    private int pageType;
    /**
     * 上一个页面传过来的用户id
     */
    private long userId;
    /// private AuthBean mAuthBean;
    /**
     * 页面显示给用户
     */
    private boolean mIsVisibleToUser;

    @Override
    protected CommonAdapter<UserInfoBean> getAdapter() {
        return new FollowFansListAdapter(getContext(), R.layout.item_follow_fans_list, mListDatas, pageType, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        DaggerFollowFansListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .followFansListPresenterModule(new FollowFansListPresenterModule(FollowFansListFragment.this))
                .build().inject(this);
        pageType = getArguments().getInt(PAGE_TYPE, FOLLOW_FRAGMENT_PAGE);
        userId = getArguments().getLong(PAGE_DATA);
        super.initView(rootView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisibleToUser = isVisibleToUser;
        if (mIsVisibleToUser && pageType == FANS_FRAGMENT_PAGE && mPresenter != null) {
            //清除粉丝未读数
            mPresenter.cleanNewFans();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsVisibleToUser && pageType == FANS_FRAGMENT_PAGE) {
            //清除粉丝未读数
            mFollowFansListPresenter.cleanNewFans();
        }
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, userId, pageType);
    }

    @Override
    protected void requestCacheData(Long maxId, boolean isLoadMore) {
        mPresenter.requestCacheData(maxId, isLoadMore, userId, pageType);
    }

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    public void upDateFollowFansState(int index) {
        // 关注页面,并且取消了关注则删除该条 item
        if (getPageType() == FOLLOW_FRAGMENT_PAGE && !mListDatas.get(index).isFollowing()) {
            mListDatas.remove(index);
            refreshData();
        } else {
            refreshData(index);
        }
    }

    @Override
    public void upDateFollowFansState() {
        refreshData();
    }

    @Override
    public int getPageType() {
        return pageType;
    }

}
