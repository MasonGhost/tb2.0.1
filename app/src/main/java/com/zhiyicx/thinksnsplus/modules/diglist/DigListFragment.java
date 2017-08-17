package com.zhiyicx.thinksnsplus.modules.diglist;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;


/**
 * @Author Jliuer
 * @Date 2017/8/17 18:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DigListFragment extends TSListFragment<DigListContract.Presenter, BaseListBean> implements DigListContract.View {
    public static final String DIG_LIST_TYPE = "dig_list_type";// 传入点赞榜的数据
    public static final String DIG_LIST_SOURCE_ID = "dig_list_source_id";
    public static final String DIG_LIST_DYNAMIC = "dynamic";// 动态
    public static final String DIG_LIST_ANSWER = "answer";// 回答

    private String type;
    private long id;


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        type = getArguments().getString(DIG_LIST_TYPE);
        id = getArguments().getLong(DIG_LIST_SOURCE_ID);
        super.initData();
    }

    @Override
    protected MultiItemTypeAdapter<BaseListBean> getAdapter() {
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
        mPresenter.requestNetData(maxId, isLoadMore, id);
    }

    @Override
    protected List<BaseListBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, type);
    }

    @Override
    public void upDataFollowState(int position) {
        refreshData(position);
    }

    @Override
    public BaseListBean getDynamicBean() {
        return null;
    }

    public static DigListFragment initFragment(Bundle bundle) {
        DigListFragment digListFragment = new DigListFragment();
        digListFragment.setArguments(bundle);
        return digListFragment;
    }
}
