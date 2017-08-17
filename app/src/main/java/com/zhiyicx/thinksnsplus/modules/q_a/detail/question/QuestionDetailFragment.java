package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.answer.PublishAnswerActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.AnswerEmptyItem;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.AnswerListItem;
import com.zhiyicx.baseproject.widget.QuestionDetailMenuView;
import com.zhiyicx.thinksnsplus.widget.QuestionInviteUserPopWindow;
import com.zhiyicx.thinksnsplus.widget.QuestionSelectListTypePopWindow;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QuestionDetailFragment extends TSListFragment<QuestionDetailContract.Presenter, AnswerInfoBean>
        implements QuestionDetailContract.View, QuestionDetailHeader.OnActionClickListener, QuestionSelectListTypePopWindow.OnOrderTypeSelectListener {

    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.qa_detail_tool)
    DynamicDetailMenuView mQaDetailTool;
    @BindView(R.id.behavior_demo_coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    private QAListInfoBean mQaListInfoBean;
    private QuestionDetailHeader mQuestionDetailHeader;
    private String mCurrentOrderType;

    private QuestionSelectListTypePopWindow mOrderTypeSelectPop; // 选择排序的弹框
    private QuestionInviteUserPopWindow mInvitePop; // 邀请回答的弹框

    public QuestionDetailFragment instance(Bundle bundle) {
        QuestionDetailFragment fragment = new QuestionDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mQaListInfoBean = (QAListInfoBean) getArguments().getSerializable(BUNDLE_QUESTION_BEAN);
        initHeaderView();
        initBottomToolStyle();
        initBottomToolListener();
        initListener();
        initPopWindow();
        mCurrentOrderType = mQuestionDetailHeader.getCurrentOrderType();
    }

    @Override
    protected void initData() {
        super.initData();
        if (mQaListInfoBean != null) {
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
    protected int getBodyLayoutId() {
        return R.layout.fragment_qusetion_detail;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    public void setQuestionDetail(QAListInfoBean questionDetail) {
        this.mQaListInfoBean = questionDetail;
        mQuestionDetailHeader.setDetail(questionDetail);
        onNetResponseSuccess(mQaListInfoBean.getAnswerInfoBeanList(), false);
    }

    @Override
    public QAListInfoBean getCurrentQuestion() {
        return mQaListInfoBean;
    }

    @Override
    public String getCurrentOrderType() {
        return mCurrentOrderType;
    }

    @Override
    public int getRealSize() {
        int size = mListDatas.size();
        if (mQaListInfoBean != null) {
            if (mQaListInfoBean.getAnswerInfoBeanList() != null) {
                size = size - mQaListInfoBean.getAnswerInfoBeanList().size();
            }
            if (mQaListInfoBean.getAdoption_answers() != null) {
                size = size - mQaListInfoBean.getAdoption_answers().size();
            }
        }
        return size;
    }

    @Override
    public void updateFollowState() {
        mQuestionDetailHeader.updateFollowState(mQaListInfoBean);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<AnswerInfoBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            if (data.isEmpty()) { // 空白展位图
                AnswerInfoBean emptyData = new AnswerInfoBean();
                data.add(emptyData);
            }
        }
        closeLoadingView();
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onFollowClick() {
        mPresenter.handleFollowState(mQaListInfoBean.getId() + "", !mQaListInfoBean.isWatched());
    }

    @Override
    public void onRewardTypeClick(List<UserInfoBean> invitations, int rewardType) {
        if (mQaListInfoBean.getAmount() == 0){
            // 跳转设置悬赏
        } else if (invitations != null){
            // 弹出邀请的人
            if (mInvitePop != null){
                mInvitePop.show();
            }
        }
    }

    @Override
    public void onAddAnswerClick() {
        // 跳转发布回答
        Intent intent = new Intent(getContext(), PublishAnswerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onChangeListOrderClick(String orderType) {
        // 弹出排序选择框
        mOrderTypeSelectPop.show();
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

    private void initBottomToolStyle() {
        // 初始化底部工具栏数据
        mQaDetailTool.setImageNormalResourceIds(new int[]{R.mipmap.home_ico_good_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.home_ico_more, R.mipmap.detail_ico_edit_normal, R.mipmap.detail_ico_good_uncollect
        });
        mQaDetailTool.setImageCheckedResourceIds(new int[]{R.mipmap.home_ico_good_high
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.home_ico_more, R.mipmap.detail_ico_edit_normal, R.mipmap.detail_ico_collect
        });
        mQaDetailTool.setButtonText(new int[]{R.string.dynamic_like, R.string.comment
                , R.string.share, R.string.more, R.string.qa_detail_edit, R.string.collect});
        mQaDetailTool.showQuestionTool(false);
        mQaDetailTool.setData();
    }

    private void initBottomToolListener() {
        mQaDetailTool.setItemOnClick((parent, v, position) -> {
            mQaDetailTool.getTag(R.id.view_data);
            switch (position) {
                case DynamicDetailMenuView.ITEM_POSITION_1:// 跳转评论页

                    break;
                case DynamicDetailMenuView.ITEM_POSITION_2:// 分享

                    break;
                case DynamicDetailMenuView.ITEM_POSITION_4:// 编辑
                    // 发布者
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_5:// 收藏
                    // 非发布者
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_3:// 更多
//                    initDealInfoMationPopupWindow(mInfoMation, mInfoMation.getHas_collect());
//                    mDealInfoMationPopWindow.show();
                    break;
                default:
                    break;
            }
        });
    }

    private void initListener() {
        mCoordinatorLayout.setEnabled(false);
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> getActivity().finish());
    }

    private void initPopWindow(){
        mOrderTypeSelectPop = QuestionSelectListTypePopWindow.Builder()
                .with(getActivity())
                .parentView(mQuestionDetailHeader.getQuestionHeaderView())
                .alpha(1f)
                .setListener(this)
                .build();
        if (mQaListInfoBean.getInvitations() != null && mQaListInfoBean.getInvitations().size() > 0){
            mInvitePop = QuestionInviteUserPopWindow.Builder()
                    .with(getActivity())
                    .parentView(mQuestionDetailHeader.getQuestionHeaderView())
                    .setData(mQaListInfoBean.getInvitations().get(0))
                    .alpha(1f)
                    .build();
        }
    }

    @Override
    public void onOrderTypeSelected(int type) {
        mQuestionDetailHeader.setCurrentOrderType(type);
        mCurrentOrderType = type == 0 ? QuestionDetailHeader.ORDER_DEFAULT : QuestionDetailHeader.ORDER_BY_TIME;
        requestNetData(0L, false);
    }
}
