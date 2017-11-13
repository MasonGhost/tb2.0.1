package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/8/18 10:22
 * @Email Jliuer@aliyun.com
 * @Description 
 */
public class AnswerDigListFragment extends TSListFragment<AnswerDigListContract.Presenter, AnswerDigListBean> implements AnswerDigListContract.View {
    public static final String DIG_LIST_DATA = "dig_list_data";// 传入点赞榜的数据

    private AnswerInfoBean mAnswerInfoBean;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        mAnswerInfoBean = getArguments().getParcelable(DIG_LIST_DATA);
        super.initData();
    }

    @Override
    protected MultiItemTypeAdapter<AnswerDigListBean> getAdapter() {
        return new AnswerDigListAdapter(getContext(), R.layout.item_dig_list, mListDatas, mPresenter);
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
        mPresenter.requestNetData(maxId, isLoadMore, mAnswerInfoBean.getId());
    }

    @Override
    protected void requestCacheData(Long maxId, boolean isLoadMore) {
         mPresenter.requestCacheData(maxId, isLoadMore, mAnswerInfoBean);
    }

    @Override
    public void upDataFollowState(int position) {
        refreshData(position);
    }

    @Override
    public AnswerInfoBean getAnswerInfoBean() {
        return mAnswerInfoBean;
    }

    public static AnswerDigListFragment initFragment(Bundle bundle) {
        AnswerDigListFragment answerDigListFragment = new AnswerDigListFragment();
        answerDigListFragment.setArguments(bundle);
        return answerDigListFragment;
    }
}
