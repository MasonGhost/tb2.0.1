package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.reward.QARewardFragment.BUNDLE_RESULT;

/**
 * @author Catherine
 * @describe 搜索专家、邀请回答的页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertSearchFragment extends TSListFragment<ExpertSearchContract.Presenter, ExpertBean>
        implements ExpertSearchContract.View {

    @BindView(R.id.fragment_search_back)
    ImageView mFragmentInfoSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_info_search_cancle)
    TextView mFragmentInfoSearchCancel;
    @BindView(R.id.tv_recommend_hint)
    TextView mTvRecommendHint;

    public ExpertSearchFragment instance(Bundle bundle) {
        ExpertSearchFragment fragment = new ExpertSearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        initListener();
    }

    private void initListener() {
        RxView.clicks(mFragmentInfoSearchCancel)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> getActivity().finish());
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(maxId, 3, isLoadMore);
    }

    private void requestNetData(Long maxId, int topic_id, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, topic_id, isLoadMore);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_search_expert;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        SearchExpertAdapter adapter = new SearchExpertAdapter(getContext(), mListDatas);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // 点击直接回到悬赏页面
                ExpertBean expertBean = mListDatas.get(position);
                if (expertBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_RESULT, expertBean);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }
}
