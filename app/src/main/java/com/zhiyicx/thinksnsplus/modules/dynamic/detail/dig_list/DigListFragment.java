package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public class DigListFragment extends TSListFragment<DigListContract.Presenter, FollowFansBean> implements DigListContract.View {
    public static final String DIG_LIST_DATA = "dig_list_data";// 传入点赞榜的数据
    private List<FollowFansBean> mDatas = new ArrayList<>();
    @Inject
    public DigListPresenter mDigListPresenter;
    // 从动态详情传递过来的动态数据，已经包括了第一页的点赞列表，所以不需要从数据库拿取
    private DynamicBean mDynamicBean;

    @Override
    protected void initView(View rootView) {
        DaggerDigListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .digListPresenterModule(new DigListPresenterModule(this))
                .build().inject(this);
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        mDynamicBean = getArguments().getParcelable(DIG_LIST_DATA);
        super.initData();
    }

    @Override
    protected MultiItemTypeAdapter<FollowFansBean> getAdapter() {
        return new DigListAdapter(getContext(), R.layout.item_dig_list, mDatas, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void setPresenter(DigListContract.Presenter presenter) {
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
    protected String setCenterTitle() {
        return getString(R.string.dig_list);
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, mDynamicBean.getFeed_id());
    }

    @Override
    protected List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, mDynamicBean);
    }

    @Override
    public void upDataFollowState(int position) {
        refreshData(position);
    }

    @Override
    public DynamicBean getDynamicBean() {
        return mDynamicBean;
    }

    public static DigListFragment initFragment(Bundle bundle) {
        DigListFragment digListFragment = new DigListFragment();
        digListFragment.setArguments(bundle);
        return digListFragment;
    }
}
