package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.QuestionTopicsAdapter;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity;
import com.zhiyicx.thinksnsplus.widget.QuestionDetailContent;
import com.zhiyicx.thinksnsplus.widget.QuestionInviteUserPopWindow;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity.BUNDLE_TOPIC_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/16
 * @contact email:648129313@qq.com
 */

public class QuestionDetailHeader implements TagFlowLayout.OnTagClickListener {

    public static final String ORDER_DEFAULT = "default";
    public static final String ORDER_BY_TIME = "time";

    private Activity mContext;

    private View mQuestionHeaderView;
    private TagFlowLayout mTflQuestion;
    private TextView mTvQuestionTitle;
    private QuestionDetailContent mQdContent;
    private TextView mTvQuestionFeedCount;
    private TextView mTvQuestionReward;
    private TextView mTvQuestionOnlookAmount;
    private CheckBox mTvTopicChangeFollow;
    private ImageView mIvRewardType;
    private TextView mTvRewardType;
    private ImageView mIvAddAnswer;
    private TextView mTvAddAnswer;
    private TextView mTvAnswerCount;
    private TextView mTvChangeOrder;
    private LinearLayout mLlAnswerInfo, rewardType, addAnswer;

    private QAListInfoBean mQaListInfoBean;
    private OnActionClickListener mListener;
    private String mCurrentOrderType = ORDER_DEFAULT;

    private QuestionInviteUserPopWindow mInvitePop; // 邀请回答的弹框

    public View getQuestionHeaderView() {
        return mQuestionHeaderView;
    }

    public QuestionDetailHeader(Activity context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mQuestionHeaderView = LayoutInflater.from(context).inflate(R.layout.header_question_detail, null);
        mTflQuestion = (TagFlowLayout) mQuestionHeaderView.findViewById(R.id.tfl_question);
        mTvQuestionTitle = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_title);
        mQdContent = (QuestionDetailContent) mQuestionHeaderView.findViewById(R.id.qd_content);
        mTvQuestionFeedCount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_feed_count);
        mTvQuestionReward = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_detail_reward);
        mTvQuestionOnlookAmount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_onlook_amount);
        mTvTopicChangeFollow = (CheckBox) mQuestionHeaderView.findViewById(R.id.tv_topic_change_follow);
        mIvRewardType = (ImageView) mQuestionHeaderView.findViewById(R.id.iv_reward_type);
        mTvRewardType = (TextView) mQuestionHeaderView.findViewById(R.id.tv_reward_type);
        mTvAddAnswer = (TextView) mQuestionHeaderView.findViewById(R.id.tv_add_answer);
        rewardType = (LinearLayout) mQuestionHeaderView.findViewById(R.id.ll_reward_type);
        addAnswer = (LinearLayout) mQuestionHeaderView.findViewById(R.id.ll_add_answer);
        mTvAnswerCount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_answer_count);
        mTvChangeOrder = (TextView) mQuestionHeaderView.findViewById(R.id.tv_change_order);
        mLlAnswerInfo = (LinearLayout) mQuestionHeaderView.findViewById(R.id.ll_answer_info);
        mIvAddAnswer = (ImageView) mQuestionHeaderView.findViewById(R.id.iv_add_answer);
    }

    public void setDetail(QAListInfoBean qaListInfoBean, double amount, int ratio) {
        if (qaListInfoBean == null) {
            return;
        }
        mQaListInfoBean = qaListInfoBean;
        // 标签信息
        List<QATopicBean> tagBeanList = qaListInfoBean.getTopics();
        if (tagBeanList != null && tagBeanList.size() > 0) {
            QuestionTopicsAdapter mUserInfoTagsAdapter = new QuestionTopicsAdapter(tagBeanList, mContext);
            mTflQuestion.setAdapter(mUserInfoTagsAdapter);
            mTflQuestion.setOnTagClickListener(this);
        }
        // 标题信息
        mTvQuestionTitle.setText(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, qaListInfoBean.getSubject()));
        // 正文
        mQdContent.setQuestionDetail(qaListInfoBean);

        boolean hasReward = qaListInfoBean.getAmount() > 0;
        // 关注
        mTvQuestionFeedCount.setText(String.format(mContext.getString(!hasReward ? R.string.qa_show_question_followed_count :
                        R.string.qa_show_question_followed_count_),
                qaListInfoBean.getWatchers_count()));


        //悬赏金额
        mTvQuestionReward.setVisibility(hasReward ? View.VISIBLE : View.GONE);
        // 是否有围观
        double outLookAmount = 0d;
        if (qaListInfoBean.getInvitation_answers() != null && !qaListInfoBean.getInvitation_answers().isEmpty()) {
            outLookAmount = Double.parseDouble(qaListInfoBean.getInvitation_answers().get(0).getOnlookers_total());
        }
        double rewardMoney =qaListInfoBean.getAmount();
        String rewardstr = String.format(mContext.getString(outLookAmount == 0 ? R.string.qa_show_question_reward_count :
                        R.string.qa_show_question_reward_count_),
                ConvertUtils.numberConvert((int) rewardMoney));
        CharSequence chars = ColorPhrase.from(rewardstr).withSeparator("[]")
                .innerColor(ContextCompat.getColor(mContext, R.color.withdrawals_item_enable))
                .outerColor(ContextCompat.getColor(mContext, R.color.general_for_hint))
                .format();

        mTvQuestionReward.setText(chars);


        updateOutLook(qaListInfoBean.getLook() == 1, outLookAmount, ratio);
        initListener();
        // 是否关注了这个话题
        updateFollowState(qaListInfoBean, ratio);
        // 答案条数
        updateAnswerView(qaListInfoBean);
        // 悬赏信息
        updateRewardType(qaListInfoBean, ratio);
        // 是否已经回答了这个问题
        updateIsAddedAnswerState(qaListInfoBean);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        QATopicBean qaTopicBean = mQaListInfoBean.getTopics().get(position);
        Intent intent = new Intent(mContext, TopicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TOPIC_BEAN, qaTopicBean);
        intent.putExtra(BUNDLE_TOPIC_BEAN, bundle);
        mContext.startActivity(intent);
        return true;
    }

    /**
     * 更新悬赏状态 amount == 0 表示还没有设置悬赏 点击可以设置悬赏
     * amount != 0 && anonymity == 0 表示已设置但是没有邀请
     *
     * @param qaListInfoBean bean
     */
    public void updateRewardType(QAListInfoBean qaListInfoBean, int ratio) {
        boolean hasReward = qaListInfoBean.getAmount() > 0;
        // 关注
        mTvQuestionFeedCount.setText(String.format(mContext.getString(!hasReward ? R.string.qa_show_question_followed_count :
                        R.string.qa_show_question_followed_count_),
                qaListInfoBean.getWatchers_count()));


        //悬赏金额
        mTvQuestionReward.setVisibility(hasReward ? View.VISIBLE : View.GONE);
        // 是否有围观
        double outLookAmount = 0d;
        if (qaListInfoBean.getInvitation_answers() != null && !qaListInfoBean.getInvitation_answers().isEmpty()) {
            outLookAmount = Double.parseDouble(qaListInfoBean.getInvitation_answers().get(0).getOnlookers_total());
        }
        double rewardMoney = qaListInfoBean.getAmount();
        String rewardstr = String.format(mContext.getString(outLookAmount == 0 ? R.string.qa_show_question_reward_count :
                        R.string.qa_show_question_reward_count_),
                ConvertUtils.numberConvert((int) rewardMoney));
        CharSequence chars = ColorPhrase.from(rewardstr).withSeparator("[]")
                .innerColor(ContextCompat.getColor(mContext, R.color.withdrawals_item_enable))
                .outerColor(ContextCompat.getColor(mContext, R.color.general_for_hint))
                .format();

        mTvQuestionReward.setText(chars);


        // 悬赏状态
        if (qaListInfoBean.getAmount() == 0) {
            mTvRewardType.setText(mContext.getString(R.string.qa_not_set_reward));
        } else if (qaListInfoBean.getInvitations() != null
                && qaListInfoBean.getInvitations().size() > 0) {
            mIvRewardType.setImageResource(R.mipmap.ico_question_invited);
            mTvRewardType.setText(mContext.getString(R.string.qa_reward_invited));
        } else {
            mTvRewardType.setText(mContext.getString(R.string.qa_reward_setting));
        }
    }

    /**
     * 更新关注状态
     *
     * @param qaListInfoBean bean
     */
    public void updateFollowState(QAListInfoBean qaListInfoBean, int ratio) {
        boolean hasReward = qaListInfoBean.getAmount() > 0;
        // 关注
        mTvQuestionFeedCount.setText(String.format(mContext.getString(!hasReward ? R.string.qa_show_question_followed_count :
                        R.string.qa_show_question_followed_count_),
                qaListInfoBean.getWatchers_count()));


        //悬赏金额
        mTvQuestionReward.setVisibility(hasReward ? View.VISIBLE : View.GONE);
        // 是否有围观
        double outLookAmount = 0d;
        if (qaListInfoBean.getInvitation_answers() != null && !qaListInfoBean.getInvitation_answers().isEmpty()) {
            outLookAmount = Double.parseDouble(qaListInfoBean.getInvitation_answers().get(0).getOnlookers_total());
        }
        double rewardMoney = qaListInfoBean.getAmount();
        String rewardstr = String.format(mContext.getString(outLookAmount == 0 ? R.string.qa_show_question_reward_count :
                        R.string.qa_show_question_reward_count_),
                ConvertUtils.numberConvert((int) rewardMoney));
        CharSequence chars = ColorPhrase.from(rewardstr).withSeparator("[]")
                .innerColor(ContextCompat.getColor(mContext, R.color.withdrawals_item_enable))
                .outerColor(ContextCompat.getColor(mContext, R.color.general_for_hint))
                .format();

        mTvQuestionReward.setText(chars);

        mTvTopicChangeFollow.setChecked(qaListInfoBean.getWatched());
        mTvTopicChangeFollow.setText(qaListInfoBean.getWatched() ? mContext.getString(R.string.followed) : mContext.getString(R.string.follow));
        mTvTopicChangeFollow.setPadding(qaListInfoBean.getWatched() ? mContext.getResources().getDimensionPixelSize(R.dimen.spacing_small) :
                mContext.getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
    }

    /**
     * 更新回答条数
     */
    public void updateAnswerView(QAListInfoBean qaListInfoBean) {
        mTvAnswerCount.setText(String.format(mContext.getString(R.string.qa_answer_count), qaListInfoBean.getAnswers_count()));
        mLlAnswerInfo.setVisibility(qaListInfoBean.getAnswers_count() == 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 更新围观信息
     */
    public void updateOutLook(boolean onlook, double amount, int ratio) {
        mTvQuestionOnlookAmount.setVisibility(onlook && amount > 0 ? View.VISIBLE : View.GONE);
        mTvQuestionOnlookAmount.setText(String.format(mContext.getString(R.string.qa_watch_amount_count), amount));
    }

    /**
     * 更新是否已经回答的状态
     */
    protected void updateIsAddedAnswerState(QAListInfoBean qaListInfoBean) {
        if (qaListInfoBean.getMy_answer() != null) {
            mTvAddAnswer.setText(mContext.getString(R.string.qa_go_to_answer));
            mIvAddAnswer.setVisibility(View.GONE);
        } else {
            mTvAddAnswer.setText(mContext.getString(R.string.qa_add_answer));
            mIvAddAnswer.setVisibility(View.VISIBLE);
        }
    }

    public Bitmap getShareBitmap() {
        return mQdContent.getShareBitmap();
    }

    private void initListener() {
        RxView.clicks(mTvTopicChangeFollow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    // 修改关注状态
                    if (mListener != null) {
                        mListener.onFollowClick();
                    }
                });
        RxView.clicks(addAnswer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null) {
                        mListener.onAddAnswerClick(mQaListInfoBean.getMy_answer());
                    }
                });
        RxView.clicks(mTvChangeOrder)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null) {
                        mListener.onChangeListOrderClick(mCurrentOrderType);
                    }
                });
        RxView.clicks(rewardType)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    // 是否已经有已采纳的答案
                    boolean isNotAdoption = mQaListInfoBean.getAdoption_answers() == null || mQaListInfoBean.getAdoption_answers().size() == 0;
                    if (mQaListInfoBean.getInvitations() != null
                            && mQaListInfoBean.getInvitations().size() > 0) {
                        initPop();
                    } else if (isNotAdoption && mQaListInfoBean.getAmount() == 0) {
                        if (mListener != null) {
                            mListener.onRewardTypeClick(null, 1);
                        }
                    }
                });
    }

    private void initPop() {
        if (mInvitePop == null) {
            mInvitePop = QuestionInviteUserPopWindow.Builder()
                    .with(mContext)
                    .parentView(mIvRewardType)
                    .setData(mQaListInfoBean.getInvitations().get(0))
                    .alpha(1f)
                    .build();
        }
        mInvitePop.show();
    }

    public String getCurrentOrderType() {
        return mCurrentOrderType;
    }

    public void setCurrentOrderType(int type) {
        mCurrentOrderType = type == 0 ? ORDER_DEFAULT : ORDER_BY_TIME;
        mTvChangeOrder.setText(type == 0 ? mContext.getString(R.string.qa_answer_list_order_default) :
                mContext.getString(R.string.qa_answer_list_order_by_time));
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.mListener = listener;
    }

    public interface OnActionClickListener {
        void onFollowClick();

        void onRewardTypeClick(List<UserInfoBean> invitations, int rewardType);

        void onAddAnswerClick(AnswerInfoBean answerInfoBean);

        void onChangeListOrderClick(String orderType);
    }
}
