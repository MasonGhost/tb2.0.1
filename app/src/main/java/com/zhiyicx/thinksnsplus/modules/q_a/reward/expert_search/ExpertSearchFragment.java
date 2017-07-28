package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe 搜索专家、邀请回答的页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertSearchFragment extends TSListFragment<ExpertSearchContract.Presenter, ExpertBean>
        implements ExpertSearchContract.View {

    @BindView(R.id.fragment_info_search_back)
    ImageView mFragmentInfoSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_info_search_cancle)
    TextView mFragmentInfoSearchCancle;

    public ExpertSearchFragment instance(Bundle bundle) {
        ExpertSearchFragment fragment = new ExpertSearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        hideLoading();
        initListener();
        mListDatas.add(new ExpertBean());
        mListDatas.add(new ExpertBean());
        mListDatas.add(new ExpertBean());
        mListDatas.add(new ExpertBean());
        mListDatas.add(new ExpertBean());
        mListDatas.add(new ExpertBean());
        mListDatas.add(new ExpertBean());
    }

    private void initListener(){
        RxView.clicks(mFragmentInfoSearchCancle)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> getActivity().finish());
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
