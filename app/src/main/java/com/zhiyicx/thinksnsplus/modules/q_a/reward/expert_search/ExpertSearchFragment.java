package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.reward.QARewardFragment.BUNDLE_RESULT;
import static com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search.ExpertSearchActivity.BUNDLE_TOPIC_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search.ExpertSearchActivity.BUNDLE_TOPIC_IDS;

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
    @BindView(R.id.fragment_info_search_container)
    RelativeLayout mFragmentInfoSearchContainer;
    @BindView(R.id.toolbar_container)
    AppBarLayout mToolbarContainer;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;

    private QATopicBean mQaTopicBean;
    private String topic_ids = "";

    public ExpertSearchFragment instance(Bundle bundle) {
        ExpertSearchFragment fragment = new ExpertSearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }


    @Override
    protected void initView(View rootView) {
        if (getArguments() != null && getArguments().containsKey(BUNDLE_TOPIC_BEAN)) {
            mQaTopicBean = (QATopicBean) getArguments().getSerializable(BUNDLE_TOPIC_BEAN);
            mTvRecommendHint.setVisibility(View.GONE);
            mFragmentInfoSearchContainer.setVisibility(View.GONE);
            mToolbarContainer.setVisibility(View.VISIBLE);
        }
        if (getArguments() != null && getArguments().containsKey(BUNDLE_TOPIC_IDS)) {
            topic_ids = getArguments().getString(BUNDLE_TOPIC_IDS);
            mTvRecommendHint.setVisibility(View.VISIBLE);
            mFragmentInfoSearchContainer.setVisibility(View.VISIBLE);
            mToolbarContainer.setVisibility(View.GONE);
        }
        super.initView(rootView);
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
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> getActivity().finish());

        RxTextView.editorActionEvents(mFragmentInfoSearchEdittext).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                if(TextUtils.isEmpty(mFragmentInfoSearchEdittext.getText())){
                    mTvRecommendHint.setVisibility(View.VISIBLE);
                    requestNetData(DEFAULT_PAGE_MAX_ID, false);
                }else {
                    mTvRecommendHint.setVisibility(View.GONE);
                    mPresenter.requestNetData(0, null, mFragmentInfoSearchEdittext.getText().toString(), false);
                }
            }
        });

        RxTextView.textChanges(mFragmentInfoSearchEdittext)
                .filter(charSequence -> charSequence.length() == 0 && mFragmentInfoSearchContainer.getVisibility() == View.VISIBLE)
                .subscribe(charSequence -> mTvRecommendHint.setVisibility(View.VISIBLE));
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        if (mQaTopicBean != null) {
            requestNetData(maxId, mQaTopicBean.getId().intValue(), isLoadMore);
        } else if (!TextUtils.isEmpty(topic_ids)) {
            requestNetData(maxId.intValue(), topic_ids, isLoadMore);
        }
    }

    private void requestNetData(Long maxId, int topic_id, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, topic_id, isLoadMore);
    }

    private void requestNetData(int size, String topic_ids, boolean isLoadMore) {
        mPresenter.requestNetData(size, topic_ids, TextUtils.isEmpty(mFragmentInfoSearchEdittext.getText().toString())
                ? null : mFragmentInfoSearchEdittext.getText().toString(), isLoadMore);
    }

    @Override
    protected Long getMaxId(@NotNull List<ExpertBean> data) {
        return (long) mListDatas.size();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_search_expert;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        SearchExpertAdapter adapter = new SearchExpertAdapter(getContext(), mListDatas, mPresenter, mQaTopicBean != null);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // 点击直接回到悬赏页面
                ExpertBean expertBean = mListDatas.get(position);
                if (expertBean != null) {
                    if (mQaTopicBean != null) {
                        UserInfoBean userInfoBean = new UserInfoBean();
                        userInfoBean.setUser_id((long) expertBean.getExtra().getUser_id());
                        userInfoBean.setFollower(expertBean.isFollower());
                        userInfoBean.setName(expertBean.getName());
                        userInfoBean.setVerified(expertBean.getVerified());
                        boolean isJoined = expertBean.isFollower();
                        userInfoBean.setFollower(isJoined);
                        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BUNDLE_RESULT, expertBean);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
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
