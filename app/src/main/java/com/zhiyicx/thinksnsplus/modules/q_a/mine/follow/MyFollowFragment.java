package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter.MyFollowQuestionAdapter;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter.QuestionTopicAdapter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity.BUNDLE_TOPIC_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyFollowContainerFragment.TYPE_QUESTION;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity.BUNDLE_MY_QUESTION_TYPE;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyFollowFragment extends TSListFragment<MyFollowContract.Presenter, BaseListBean>
        implements MyFollowContract.View, MultiItemTypeAdapter.OnItemClickListener{

    @Inject
    MyFollowPresenter mFollowPresenter;

    private String mType;

    public MyFollowFragment instance(String type){
        MyFollowFragment followFragment = new MyFollowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MY_QUESTION_TYPE, type);
        followFragment.setArguments(bundle);
        return followFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (TextUtils.isEmpty(mType)){
            mType = getArguments().getString(BUNDLE_MY_QUESTION_TYPE);
        }
        CommonAdapter adapter;
        if (mType.equals(TYPE_QUESTION)){
            adapter = new MyFollowQuestionAdapter(getContext(), mListDatas);
        } else{
            adapter = new QuestionTopicAdapter(getContext(), mListDatas, mPresenter);
        }
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected void initData() {
        DaggerMyFollowComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myFollowPresenterModule(new MyFollowPresenterModule(this))
                .build()
                .inject(this);

        super.initData();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mType.equals(TYPE_QUESTION)){
            // 问题详情
        } else{
            // 话题详情
            if (mListDatas.get(position) instanceof QATopicBean){
                QATopicBean qaTopicBean = (QATopicBean) mListDatas.get(position);
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_TOPIC_BEAN, qaTopicBean);
                intent.putExtra(BUNDLE_TOPIC_BEAN, bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }
}
