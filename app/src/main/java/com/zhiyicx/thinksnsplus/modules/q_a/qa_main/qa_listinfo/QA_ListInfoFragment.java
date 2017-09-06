package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragment extends TSListFragment<QA_ListInfoConstact.Presenter, QAListInfoBean>
        implements QA_ListInfoConstact.View {

    public static final String BUNDLE_QA_TYPE = "qa_type";

    private String mQAInfoType;

    public String[] QA_TYPES;

    @Inject
    QA_ListInfoFragmentPresenter mQA_listInfoFragmentPresenter;

    public static QA_ListInfoFragment newInstance(String params) {
        QA_ListInfoFragment fragment = new QA_ListInfoFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_QA_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getQAInfoType() {
        return mQAInfoType;
    }

    @Override
    public void showDeleteSuccess() {
        showSnackSuccessMessage(getString(R.string.qa_question_delete_success));
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.setRefreshing(true);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initData() {
        DaggerQA_ListInfoComponent
                .builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qA_listInfoFragmentPresenterModule(new QA_listInfoFragmentPresenterModule(this))
                .build().inject(this);
        QA_TYPES = getResources().getStringArray(R.array.qa_net_type);
        mQAInfoType = getArguments().getString(BUNDLE_QA_TYPE);

        super.initData();

    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(null, maxId, mQAInfoType, isLoadMore);
    }

    private void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        mPresenter.requestNetData(subject, maxId, type, isLoadMore);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        QAListInfoAdapter adapter = new QAListInfoAdapter(getActivity(), R.layout.item_qa_content, mListDatas) {
            @Override
            protected int getExcellentTag() {
                return (getQAInfoType().equals(QA_TYPES[0]) || getQAInfoType().equals(QA_TYPES[1]) ? 0 : R.mipmap.icon_choice);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                QAListInfoBean listInfoBean = mListDatas.get(position);
                bundle.putSerializable(BUNDLE_QUESTION_BEAN, listInfoBean);
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
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE)
    public void updateList(Bundle bundle) {
        if (bundle != null) {
            QAListInfoBean qaListInfoBean = (QAListInfoBean) bundle.
                    getSerializable(EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
            if (qaListInfoBean != null) {
                for (int i = 0; i < mListDatas.size(); i++) {
                    if (qaListInfoBean.getId().equals(mListDatas.get(i).getId())) {
                        mListDatas.remove(i);
                        refreshData();
                        showDeleteSuccess();
                        break;
                    }
                }
            }
        }
    }

}
