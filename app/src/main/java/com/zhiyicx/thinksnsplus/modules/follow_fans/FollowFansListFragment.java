package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSListFragment<FollowFansListContract.Presenter, FollowFansBean> implements FollowFansListContract.View {
    // 当前页面是关注页面还是粉丝页面:pageType
    public static final int FANS_FRAGMENT_PAGE = 0;
    public static final int FOLLOW_FRAGMENT_PAGE = 1;

    // 获取页面类型的key
    public static final String PAGE_TYPE = "page_type";
    @Inject
    FollowFansListPresenter mFollowFansListPresenter;
    private List<FollowFansBean> datas = new ArrayList<>();
    private int pageType;// 页面类型，由上一个页面决定
    private AuthBean mAuthBean;

    @Override
    protected CommonAdapter<FollowFansBean> getAdapter() {
        return new FollowFansListAdapter(getContext(), R.layout.item_follow_fans_list, datas, pageType, mPresenter);
    }

    @Override
    protected void initView(View rootView) {
        DaggerFollowFansListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .followFansListPresenterModule(new FollowFansListPresenterModule(FollowFansListFragment.this))
                .build().inject(this);
        pageType = getArguments().getInt(PAGE_TYPE, FOLLOW_FRAGMENT_PAGE);
        mAuthBean = AppApplication.getmCurrentLoginAuth();
        super.initView(rootView);
    }


    @Override
    protected boolean showToolbar() {
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
    public void setPresenter(FollowFansListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, mAuthBean.getUser_id(), pageType);
    }

    @Override
    protected List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, mAuthBean.getUser_id(), pageType);
    }

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    public void upDateFollowFansState(int index, int followState) {
        List<FollowFansBean> followFansBeanList = mAdapter.getDatas();
        FollowFansBean followFansBean = followFansBeanList.get(index);
        LogUtils.i("new_state--》" + followState);
        followFansBean.setFollowState(followState);
        refreshData(index);
    }

    @Override
    public void upDateUserInfo(List<UserInfoBean> userInfoBeanList) {
        onCacheResponseSuccess(requestCacheData(mMaxId, false), false);
        refreshData();
    }

}
