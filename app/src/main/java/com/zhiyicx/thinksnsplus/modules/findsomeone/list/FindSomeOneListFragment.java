package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.DaggerFollowFansListPresenterComponent;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListAdapter;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListPresenter;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListPresenterModule;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 找人列表页
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneListFragment extends TSListFragment<FindSomeOneListContract.Presenter, UserInfoBean> implements FindSomeOneListContract.View {
    /**
     * 找人的分类
     */
    public static final int TYPE_HOT = 0;
    public static final int TYPE_NEW = 1;
    public static final int TYPE_RECOMMENT = 2;
    public static final int TYPE_NEARBY = 3;

    // 获取页面类型的key
    public static final String PAGE_TYPE = "page_type";

    @Inject
    FollowFansListPresenter mFollowFansListPresenter;
    private int pageType;// 页面类型，由上一个页面决定
    private long userId;// 上一个页面传过来的用户id
    //private AuthBean mAuthBean;

    private boolean mIsVisibleToUser;//页面显示给用户

    @Override
    protected CommonAdapter<UserInfoBean> getAdapter() {
        return new FindSomeOneListAdapter(getContext(), R.layout.item_find_some_list, mListDatas, pageType, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        DaggerFindSomeOneListPresenterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .findSomeOneListPresenterModule(new FindSomeOneListPresenterModule(FindSomeOneListFragment.this))
                .build().inject(this);
        pageType = getArguments().getInt(PAGE_TYPE, TYPE_HOT);
        //mAuthBean = AppApplication.getmCurrentLoginAuth();
        super.initView(rootView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisibleToUser = isVisibleToUser;

    }

    @Override
    public void onResume() {
        super.onResume();

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
    protected List<UserInfoBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, userId, pageType);
    }

    public static FindSomeOneListFragment initFragment(Bundle bundle) {
        FindSomeOneListFragment followFansListFragment = new FindSomeOneListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    public void upDateFollowFansState(int index) {
            refreshData(index);
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
