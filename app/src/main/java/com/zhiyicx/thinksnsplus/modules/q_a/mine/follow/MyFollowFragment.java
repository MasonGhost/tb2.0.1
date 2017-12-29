package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter.MyFollowQuestionAdapter;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter.QuestionTopicAdapter;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.DaggerMyAnswerComponent;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerPresenterModule;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity.BUNDLE_TOPIC_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyFollowContainerFragment.TYPE_QUESTION;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyFollowContainerFragment.TYPE_TOPIC;
import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity.BUNDLE_MY_QUESTION_TYPE;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyFollowFragment extends TSListFragment<MyFollowContract.Presenter, BaseListBean>
        implements MyFollowContract.View, MultiItemTypeAdapter.OnItemClickListener {

    @Inject
    MyFollowPresenter mFollowPresenter;

    private String mType;

    public static MyFollowFragment instance(String type) {
        MyFollowFragment followFragment = new MyFollowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MY_QUESTION_TYPE, type);
        followFragment.setArguments(bundle);
        return followFragment;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {

        super.initView(rootView);
        Observable.create(subscriber -> {

            DaggerMyFollowComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .myFollowPresenterModule(new MyFollowPresenterModule(MyFollowFragment.this))
                    .build()
                    .inject(MyFollowFragment.this);

            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        if (TextUtils.isEmpty(mType)) {
            mType = getArguments().getString(BUNDLE_MY_QUESTION_TYPE);
        }
        CommonAdapter adapter;
        if (mType.equals(TYPE_QUESTION)) {
            adapter = new MyFollowQuestionAdapter(getContext(), mListDatas) {
                @Override
                protected int getExcellentTag(boolean isExcellent) {
                    return isExcellent ? R.mipmap.icon_choice : 0;
                }

                @Override
                protected boolean isTourist() {
                    return mPresenter.handleTouristControl();
                }

                @Override
                protected int getRatio() {
                    return mPresenter.getRatio();
                }
            };
        } else {
            adapter = new QuestionTopicAdapter(getContext(), mListDatas, mFollowPresenter);
        }
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mType.equals(TYPE_QUESTION)) {
            // 问题详情
            if (mListDatas.get(position) instanceof QAListInfoBean) {
                QAListInfoBean qaListInfoBean = (QAListInfoBean) mListDatas.get(position);
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, qaListInfoBean);
                intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
                startActivity(intent);
            }
        } else {
            // 话题详情
            if (mListDatas.get(position) instanceof QATopicBean) {
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

    @Override
    public String getType() {
        if (TextUtils.isEmpty(mType)) {
            mType = getArguments().getString(BUNDLE_MY_QUESTION_TYPE);
        }
        return mType;
    }

    @Override
    public void updateTopicFollowState(QATopicBean qaTopicBean) {
        if (mType.equals(TYPE_TOPIC)) {
            int position = -1;
            for (int i = 0; i < mListDatas.size(); i++) {
                QATopicBean topicBean = (QATopicBean) mListDatas.get(i);
                if (topicBean.getId().equals(qaTopicBean.getId())) {
                    position = i;
                    break;
                }
            }
            if (!qaTopicBean.getHas_follow()) {
                if (position != -1) {
                    mListDatas.remove(position);
                }
            } else {
                if (position == -1) {
                    mListDatas.add(qaTopicBean);
                }
            }
        }
        refreshData();
    }
}
