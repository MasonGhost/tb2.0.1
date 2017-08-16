package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.widget.QuestionDetailContent;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/16
 * @contact email:648129313@qq.com
 */

public class QuestionDetailHeader {

    private static final int ORDER_DEFAULT = 0;
    private static final int ORDER_BY_TIME = 1;

    private Context mContext;

    private View mQuestionHeaderView;
    private TagFlowLayout mTflQuestion;
    private TextView mTvQuestionTitle;
    private TextView mTvRewardAmount;
    private QuestionDetailContent mQdContent;
    private TextView mTvQuestionFeedCount;
    private TextView mTvQuestionRewardAmount;
    private CheckBox mTvTopicChangeFollow;
    private ImageView mIvRewardType;
    private TextView mTvRewardType;
    private ImageView mIvAddAnswer;
    private TextView mTvAddAnswer;
    private TextView mTvAnswerCount;
    private TextView mTvChangeOrder;

    private QAListInfoBean mQaListInfoBean;
    private OnActionClickListener mListener;
    private int mCurrentOrderType = ORDER_DEFAULT;

    public View getQuestionHeaderView() {
        return mQuestionHeaderView;
    }

    public QuestionDetailHeader(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mQuestionHeaderView = LayoutInflater.from(context).inflate(R.layout.header_question_detail, null);
        mTflQuestion = (TagFlowLayout) mQuestionHeaderView.findViewById(R.id.tfl_question);
        mTvQuestionTitle = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_title);
        mTvRewardAmount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_reward_amount);
        mQdContent = (QuestionDetailContent) mQuestionHeaderView.findViewById(R.id.qd_content);
        mTvQuestionFeedCount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_feed_count);
        mTvQuestionRewardAmount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_question_reward_amount);
        mTvTopicChangeFollow = (CheckBox) mQuestionHeaderView.findViewById(R.id.tv_topic_change_follow);
        mIvRewardType = (ImageView) mQuestionHeaderView.findViewById(R.id.iv_reward_type);
        mTvRewardType = (TextView) mQuestionHeaderView.findViewById(R.id.tv_reward_type);
        mTvAddAnswer = (TextView) mQuestionHeaderView.findViewById(R.id.tv_add_answer);
        mTvAnswerCount = (TextView) mQuestionHeaderView.findViewById(R.id.tv_answer_count);
        mTvChangeOrder = (TextView) mQuestionHeaderView.findViewById(R.id.tv_change_order);
    }

    public void setDetail(QAListInfoBean qaListInfoBean){
        if (qaListInfoBean == null){
            return;
        }
        mQaListInfoBean = qaListInfoBean;
        // 标签信息
        // 标题信息
        mTvQuestionTitle.setText(qaListInfoBean.getSubject());
        // 悬赏金额
        if (qaListInfoBean.getAmount() != 0){
            mTvRewardAmount.setText(String.format(mContext.getString(R.string.qa_reward_amount), qaListInfoBean.getAmount()));
        }
        // 正文
        mQdContent.setQuestionDetail(qaListInfoBean);
        // 关注回答数
        mTvQuestionFeedCount.setText(String.format(mContext.getString(R.string.qa_show_topic_followed),
                qaListInfoBean.getWatchers_count(), qaListInfoBean.getAnswers_count()));
        // 是否有围观
        if (qaListInfoBean.getLook() == 1){
            if (qaListInfoBean.getInvitation_answers() != null && qaListInfoBean.getInvitation_answers().size() > 0){
                AnswerInfoBean answerInfoBean = qaListInfoBean.getInvitation_answers().get(0);
                mTvQuestionRewardAmount.setText(String.format(mContext.getString(R.string.qa_watch_amount), answerInfoBean.getViews_count()));
            }
        }
        // 悬赏信息
        updateRewardType(qaListInfoBean);
        // 是否关注了这个话题
        mTvTopicChangeFollow.setChecked(qaListInfoBean.getWatched());
        mTvTopicChangeFollow.setText(qaListInfoBean.getWatched() ? mContext.getString(R.string.followed) : mContext.getString(R.string.follow));
        // 答案条数
        mTvAnswerCount.setText(String.format(mContext.getString(R.string.qa_answer_count), qaListInfoBean.getAnswers_count()));
        initListener();
    }

    /**
     * 更新悬赏状态 amount == 0 表示公开开悬赏 点击可以设置悬赏
     *              amount != 0 && anonymity == 0 表示已设置但是没有邀请
     * @param qaListInfoBean bean
     */
    public void updateRewardType(QAListInfoBean qaListInfoBean){
        // 悬赏状态
        if (qaListInfoBean.getAmount() == 0){
            mTvRewardType.setText(mContext.getString(R.string.qa_reward_public));
        } else if (qaListInfoBean.getAnonymity() == 0){
            mTvRewardType.setText(mContext.getString(R.string.qa_reward_setting));
        } else {
            mTvRewardType.setText(mContext.getString(R.string.qa_reward_invited));
            mIvAddAnswer.setImageResource(R.mipmap.ico_question_invited);
        }
    }

    private void initListener(){
        RxView.clicks(mTvTopicChangeFollow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    // 修改关注状态
                    if (mListener != null){
                        mListener.onFollowClick();
                    }
                });
        RxView.clicks(mTvAddAnswer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null){
                        mListener.onAddAnswerClick();
                    }
                });
        RxView.clicks(mTvChangeOrder)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null){
                        mListener.onChangeListOrderClick(mCurrentOrderType);
                    }
                });
        RxView.clicks(mTvRewardType)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null){
                        mListener.onRewardTypeClick(mQaListInfoBean.getInvitations(), 0);
                    }
                });
    }

    public void setOnActionClickListener(OnActionClickListener listener){
        this.mListener = listener;
    }

    public interface OnActionClickListener{
        void onFollowClick();
        void onRewardTypeClick(List<QAListInfoBean.UserBean> invitations, int rewardType);
        void onAddAnswerClick();
        void onChangeListOrderClick(int orderType);
    }
}
