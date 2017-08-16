package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.AnswerEmptyItem;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.AnswerListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QuestionDetailFragment extends TSListFragment<QuestionDetailContract.Presenter, AnswerInfoBean>
        implements QuestionDetailContract.View, QuestionDetailHeader.OnActionClickListener {

    private QAListInfoBean mQaListInfoBean;
    private QuestionDetailHeader mQuestionDetailHeader;

    public QuestionDetailFragment instance(Bundle bundle){
        QuestionDetailFragment fragment = new QuestionDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initHeaderView();
    }

    @Override
    protected void initData() {
        super.initData();
        mQaListInfoBean = (QAListInfoBean) getArguments().getSerializable(BUNDLE_QUESTION_BEAN);
        if (mQaListInfoBean != null){
            mPresenter.getQuestionDetail(String.valueOf(mQaListInfoBean.getId()));
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        AnswerListItem answerListItem = new AnswerListItem();
        multiItemTypeAdapter.addItemViewDelegate(answerListItem);
        multiItemTypeAdapter.addItemViewDelegate(new AnswerEmptyItem());
        return multiItemTypeAdapter;
    }

    @Override
    public void setQuestionDetail(QAListInfoBean questionDetail) {
        mQuestionDetailHeader.setDetail(questionDetail);

    }

    @Override
    public QAListInfoBean getCurrentQuestion() {
        return mQaListInfoBean;
    }

    @Override
    public void onFollowClick() {

    }

    @Override
    public void onRewardTypeClick(List<QAListInfoBean.UserBean> invitations, int rewardType) {

    }

    @Override
    public void onAddAnswerClick() {

    }

    @Override
    public void onChangeListOrderClick(int orderType) {

    }

    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mQuestionDetailHeader = new QuestionDetailHeader(getContext(), null/*mPresenter.getAdvert()*/);
        mQuestionDetailHeader.setOnActionClickListener(this);
        mHeaderAndFooterWrapper.addHeaderView(mQuestionDetailHeader.getQuestionHeaderView());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }
}
