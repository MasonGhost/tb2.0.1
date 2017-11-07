package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public class DigListFragment extends TSListFragment<DigListContract.Presenter, DynamicDigListBean> implements DigListContract.View {
    /**
     * 传入点赞榜的数据
     */
    public static final String DIG_LIST_DATA = "dig_list_data";

    /**
     * 从动态详情传递过来的动态数据，已经包括了第一页的点赞列表，所以不需要从数据库拿取
     */
    private DynamicDetailBeanV2 mDynamicBean;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        mDynamicBean = getArguments().getParcelable(DIG_LIST_DATA);
        super.initData();
    }

    @Override
    protected MultiItemTypeAdapter<DynamicDigListBean> getAdapter() {
        return new DigListAdapter(getContext(), R.layout.item_dig_list, mListDatas, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dig_list);
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, mDynamicBean.getId());
    }

    @Override
    protected void requestCacheData(Long maxId, boolean isLoadMore) {
        mPresenter.requestCacheData(maxId, isLoadMore, mDynamicBean);
    }

    @Override
    public void upDataFollowState(int position) {
        refreshData(position);
    }

    @Override
    public DynamicDetailBeanV2 getDynamicBean() {
        return mDynamicBean;
    }

    public static DigListFragment initFragment(Bundle bundle) {
        DigListFragment digListFragment = new DigListFragment();
        digListFragment.setArguments(bundle);
        return digListFragment;
    }
}
