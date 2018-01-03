package com.zhiyicx.thinksnsplus.modules.draftbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.modules.draftbox.adapter.AnswerDraftItem;
import com.zhiyicx.thinksnsplus.modules.draftbox.adapter.PostDraftItem;
import com.zhiyicx.thinksnsplus.modules.draftbox.adapter.QuestionDraftItem;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.BaseMarkdownActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.answer.PublishAnswerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.answer.PublishType;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment.BUNDLE_PUBLISHQA_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/08/22/11:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DraftBoxFragment extends TSListFragment<DraftBoxContract.Presenter, BaseDraftBean>
        implements DraftBoxContract.View, QuestionDraftItem.QuestionDraftItemEvent {


    public static final String MY_DRAFT_TYPE = "MY_DRAFT_TYPE";
    public static final String MY_DRAFT_TYPE_QUESTION = "0";
    public static final String MY_DRAFT_TYPE_ANSWER = "1";
    public static final String MY_DRAFT_TYPE_POST = "2";

    @Inject
    DraftBoxPresenter mDraftBoxPresenter;

    public static DraftBoxFragment getInstance(String type) {
        DraftBoxFragment draftBoxFragment = new DraftBoxFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MY_DRAFT_TYPE, type);
        draftBoxFragment.setArguments(bundle);
        return draftBoxFragment;
    }

    @Override
    public String getDraftType() {
        return getArguments().getString(MY_DRAFT_TYPE);
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
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestNetData(0L, false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        QuestionDraftItem questionDraftItem = new QuestionDraftItem(getActivity());
        AnswerDraftItem answerDraftItem = new AnswerDraftItem(getActivity());
        PostDraftItem postDraftItem = new PostDraftItem(getActivity());
        adapter.addItemViewDelegate(questionDraftItem);
        adapter.addItemViewDelegate(answerDraftItem);
        adapter.addItemViewDelegate(postDraftItem);
        questionDraftItem.setQuestionDraftItemEvent(this);
        answerDraftItem.setQuestionDraftItemEvent(this);
        postDraftItem.setQuestionDraftItemEvent(this);
        return adapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    public void toEditDraft(BaseDraftBean draftBean) {
        if (draftBean instanceof QAPublishBean) {
            QAPublishBean realData = (QAPublishBean) draftBean;
            Intent intent = new Intent(getActivity(), PublishQuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, realData);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (draftBean instanceof AnswerDraftBean) {
            AnswerDraftBean realData = (AnswerDraftBean) draftBean;
            PublishAnswerFragment.startQActivity(getActivity(), PublishType
                    .PUBLISH_ANSWER, realData);
        }else if (draftBean instanceof PostDraftBean){
            PostDraftBean realData = (PostDraftBean) draftBean;
            BaseMarkdownActivity.startActivityForPublishPostInDraft(mActivity,realData);
        }
    }

    @Override
    public void deleteDraft(BaseDraftBean draftBean) {
        mPresenter.deleteDraft(draftBean);
        mListDatas.remove(draftBean);
        refreshData();
    }

    @Override
    protected void initData() {
        DaggerDraftBoxComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .draftBoxPresenterModule(new DraftBoxPresenterModule(this))
                .build().inject(this);
        super.initData();
    }
}
