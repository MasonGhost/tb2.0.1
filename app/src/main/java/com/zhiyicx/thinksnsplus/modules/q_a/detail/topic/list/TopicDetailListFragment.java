package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo.QAListInfoAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class TopicDetailListFragment extends TSListFragment<TopicDetailListContract.Presenter, QAListInfoBean>
        implements TopicDetailListContract.View {

    @Inject
    TopicDetailListPresenter mPresenter;

    public static final String BUNDLE_TOPIC_TYPE = "bundle_topic_type";
    public static final String BUNDLE_TOPIC_BEAN = "bundle_topic_bean";

    private String mType;
    private QATopicBean mTopicBean;

    public String[] QA_TYPES;

    public TopicDetailListFragment instance(Bundle bundle) {
        TopicDetailListFragment fragment = new TopicDetailListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mRvList.addItemDecoration(new LinearDecoration(0, getResources().getDimensionPixelOffset(com.zhiyicx.thinksnsplus.R.dimen.spacing_small), 0, 0));

    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void initData() {
        DaggerTopicDetailListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .topicDetailListPresenterModule(new TopicDetailListPresenterModule(this))
                .build()
                .inject(this);
        super.initData();
        QA_TYPES = getResources().getStringArray(R.array.qa_net_type);
        if (TextUtils.isEmpty(mType)) {
            mType = getArguments().getString(BUNDLE_TOPIC_TYPE);
        }
        if (mTopicBean == null) {
            mTopicBean = (QATopicBean) getArguments().getSerializable(BUNDLE_TOPIC_BEAN);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        QAListInfoAdapter adapter = new QAListInfoAdapter(getActivity(), R.layout.item_qa_content, mListDatas) {
            @Override
            protected int getExcellentTag(boolean isExcellent) {
                boolean isNewOrExcellent = getCurrentType().equals(QA_TYPES[0]) || getCurrentType().equals(QA_TYPES[1]);
                return isNewOrExcellent ? 0 : (isExcellent ? R.mipmap.icon_choice : 0);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                position = position - mHeaderAndFooterWrapper.getHeadersCount();
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, mListDatas.get(position));
                intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    public String getCurrentType() {
        if (TextUtils.isEmpty(mType)) {
            mType = getArguments().getString(BUNDLE_TOPIC_TYPE);
        }
        return mType;
    }

    @Override
    public Long getTopicId() {
        if (mTopicBean == null) {
            mTopicBean = (QATopicBean) getArguments().getSerializable(BUNDLE_TOPIC_BEAN);
        }
        return mTopicBean.getId();
    }

    @Override
    public void showDeleteSuccess() {

    }

    @Override
    protected boolean showToolbar() {
        return false;
    }
}
