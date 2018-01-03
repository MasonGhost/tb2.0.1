package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.QuestionCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.QuestionCommentItem;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment.QuestionCommentActivity.BUNDLE_QUESTION_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public class QuestionCommentFragment extends TSListFragment<QuestionCommentContract.Presenter, QuestionCommentBean>
        implements QuestionCommentContract.View, QuestionCommentItem.OnCommentItemListener, InputLimitView
        .OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener {

    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.behavior_demo_coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;

    private QAListInfoBean mQaListInfoBean;

    private ActionPopupWindow mDeleteCommentPopWindow;
    private Long mReplyUserId = 0L;

    public QuestionCommentFragment instance(Bundle bundle) {
        QuestionCommentFragment fragment = new QuestionCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public QAListInfoBean getCurrentQuestion() {
        return mQaListInfoBean;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

//    @Override
//    protected String setCenterTitle() {
//        return getString(R.string.comment);
//    }


    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_question_comment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mQaListInfoBean = (QAListInfoBean) getArguments().getSerializable(BUNDLE_QUESTION_BEAN);
        initListener();
        updateCommentCount();
        showCommentView();
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        QuestionCommentItem infoDetailCommentItem = new QuestionCommentItem(this);
        multiItemTypeAdapter.addItemViewDelegate(infoDetailCommentItem);
        multiItemTypeAdapter.addItemViewDelegate(new QuestionCommentEmptyItem());
        multiItemTypeAdapter.setOnItemClickListener(this);
        return multiItemTypeAdapter;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<QuestionCommentBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            if (mQaListInfoBean.getComments_count() == 0) {// 根据内容更新一下评论数量，这个数量是不准确的
                mQaListInfoBean.setComments_count(data.size());
            }
            if (data.isEmpty()) { // 空白展位图
                QuestionCommentBean emptyData = new QuestionCommentBean();
                data.add(emptyData);
            }
        }
        updateCommentCount();
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onCommentTextClick(View view, RecyclerView.ViewHolder holder, int position) {
        comment(position);
    }

    @Override
    public void onCommentTextLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        goReportComment(position);
    }

    /**
     * 评论
     * @param position
     */
    private void comment(int position) {
        QuestionCommentBean infoCommentListBean = mListDatas.get(position);
        if (infoCommentListBean != null && !TextUtils.isEmpty(infoCommentListBean.getBody())) {
            if (infoCommentListBean.getUser_id() == AppApplication.getmCurrentLoginAuth()
                    .getUser_id()) {// 自己的评论
                initPop(infoCommentListBean, position);
                mDeleteCommentPopWindow.show();
            } else {
                mReplyUserId = infoCommentListBean.getUser_id();
                showCommentView();
                String contentHint = getString(R.string.default_input_hint);
                if (!infoCommentListBean.getReply_user().equals(mQaListInfoBean.getUser_id())) {
                    contentHint = getString(R.string.reply, infoCommentListBean.getFromUserInfoBean().getName());
                }
                mIlvComment.setEtContentHint(contentHint);
            }
        }
    }

    /**
     * 举报
     * @param position
     */
    private void goReportComment(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        // 举报
        if (mListDatas.get(position).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(mListDatas.get(position).getFromUserInfoBean(), mListDatas.get
                    (position).getId().toString(),
                    null, null, mListDatas.get(position).getBody(), ReportType.COMMENT));

        } else {

        }
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mReplyUserId.intValue(), text);
    }

    private void initListener() {
        mCoordinatorLayout.setEnabled(false);
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> getActivity().finish());
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    mIlvComment.setVisibility(View.GONE);
                    mIlvComment.clearFocus();
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                    mVShadow.setVisibility(View.GONE);

                });
        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    public void updateCommentCount() {
        mTvToolbarCenter.setText(String.format(getString(R.string.qa_question_comment_count), mQaListInfoBean.getComments_count()));
    }

    @Override
    public void setLoading(boolean isLoading, boolean isSuccess, String message) {
        if (isLoading) {
            showSnackLoadingMessage(message);
        } else {
            if (isSuccess) {
                updateCommentCount();
                showSnackSuccessMessage(message);
            } else {
                showSnackErrorMessage(message);
            }
        }
    }

    private void initPop(QuestionCommentBean questionCommentBean, int position) {
        mDeleteCommentPopWindow = ActionPopupWindow.builder()
//                .item1Str(BuildConfig.USE_TOLL ? getString(R.string.dynamic_list_top_comment) : null)
//                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 跳转置顶页面
//                    Bundle bundle = new Bundle();
//                    bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_INFO);// 资源类型
//                    bundle.putLong(StickTopFragment.PARENT_ID, mQaListInfoBean.getId());// 资源id
//                    bundle.putLong(StickTopFragment.CHILD_ID,data.getId());
//                    Intent intent = new Intent(getActivity(), StickTopActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                })
                .item2ClickListener(() -> {
                    mDeleteCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete), () -> {
                        mPresenter.deleteComment(mQaListInfoBean.getId(), questionCommentBean.getId(), position);
                    }, true);

                })
                .bottomClickListener(() -> mDeleteCommentPopWindow.hide())
                .build();
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        comment(position);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        goReportComment(position);
        return true;
    }
}
