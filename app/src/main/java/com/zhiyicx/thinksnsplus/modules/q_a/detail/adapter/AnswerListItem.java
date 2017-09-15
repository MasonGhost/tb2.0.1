package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe 答案列表item
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class AnswerListItem implements ItemViewDelegate<AnswerInfoBean> {

    private QuestionDetailContract.Presenter mPresenter;
    private OnGoToWatchClickListener mListener;
    private QAListInfoBean mQaListInfoBean;
    protected TextViewUtils.OnSpanTextClickListener mOnSpanTextClickListener;

    public AnswerListItem(@NotNull QuestionDetailContract.Presenter mPresenter, @NotNull QAListInfoBean qaListInfoBean) {
        mQaListInfoBean = qaListInfoBean;
        this.mPresenter = mPresenter;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_question_answer;
    }

    @Override
    public boolean isForViewType(AnswerInfoBean item, int position) {
        return !(TextUtils.isEmpty(item.getBody()) && item.getInvited() != 1);
    }

    @Override
    public void convert(ViewHolder holder, AnswerInfoBean answerInfoBean, AnswerInfoBean lastT, int position, int itemCounts) {

        boolean isOnlook = mQaListInfoBean.getLook() == 1; // 是否开启了围观

        boolean isInvited = answerInfoBean.getInvited() == 1;// 是否被邀请回答

        boolean canNotLook = TextUtils.isEmpty(answerInfoBean.getBody());// 是否要付费才能查看

        // 发布者信息
        boolean isMine = answerInfoBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
        ImageUtils.loadCircleUserHeadPic(answerInfoBean.getUser(), holder.getView(R.id.iv_portrait), !isMine && answerInfoBean.getAnonymity() == 1);
        TextView nameView = holder.getTextView(R.id.tv_name);
        nameView.setText(answerInfoBean.getAnonymity() == 1 && !isMine ? nameView.getResources().getString(R.string.qa_question_answer_anonymity_user) : answerInfoBean.getUser().getName());
        // 围观数量 PS：围观是只有邀请了专家来回答的才有哦
        holder.setVisible(R.id.tv_watcher_count, isOnlook && isInvited ? View.VISIBLE : View.GONE);
        if (isOnlook) {
            holder.setText(R.id.tv_watcher_count, String.format(holder.getConvertView().getContext()
                    .getString(R.string.qa_question_answer_show_watcher_count), answerInfoBean.getOnlookers_count()));
        }
        // 跳转
        RxView.clicks(holder.getView(R.id.iv_portrait))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (answerInfoBean.getAnonymity() != 1) {
                        PersonalCenterFragment.startToPersonalCenter(holder.getConvertView().getContext(), answerInfoBean.getUser());
                    }
                });

        // 是否采纳
        holder.setVisible(R.id.tv_adopt_flag, answerInfoBean.getAdoption() == 1 ? View.VISIBLE : View.GONE);
        // 是否邀请
        holder.setVisible(R.id.tv_invite_flag, answerInfoBean.getInvited() == 1 ? View.VISIBLE : View.GONE);
        // 时间
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(answerInfoBean.getCreated_at()));
        // 正文
        holder.setText(R.id.tv_content, RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, answerInfoBean.getBody()));

        String content = RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, answerInfoBean.getBody());
        TextView contentView = holder.getView(R.id.tv_content);

        if (!canNotLook) {
            TextViewUtils.newInstance(contentView, content)
                    .spanTextColor(SkinUtils.getColor(R
                            .color.normal_for_assist_text))
                    .position(0, content.length())
                    .dataPosition(position)
                    .maxLines(contentView.getResources().getInteger(R.integer
                            .dynamic_list_content_show_lines))
                    .onSpanTextClickListener(mOnSpanTextClickListener)
                    .disPlayText(true)
                    .build();
        } else {
            content += contentView.getContext().getString(R.string.words_holder);
            TextViewUtils.newInstance(contentView, content)
                    .spanTextColor(SkinUtils.getColor(R
                            .color.normal_for_assist_text))
                    .position(0, content.length())
                    .dataPosition(position)
                    .maxLines(contentView.getResources().getInteger(R.integer
                            .dynamic_list_content_show_lines))
                    .onSpanTextClickListener(mOnSpanTextClickListener)
                    .amount(10)
                    .disPlayText(false)
                    .build();
        }
        contentView.setVisibility(View.VISIBLE);


        // 点赞数量
        TextView tvLikeCount = holder.getView(R.id.tv_like_count);
        tvLikeCount.setText(String.valueOf(answerInfoBean.getLikes_count()));
        dealLikeUI(answerInfoBean, tvLikeCount);
        RxView.clicks(tvLikeCount)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (!answerInfoBean.getLiked()) {
                        answerInfoBean.setLikes_count(answerInfoBean.getLikes_count() + 1);
                    } else {
                        answerInfoBean.setLikes_count(answerInfoBean.getLikes_count() - 1);
                    }
                    mPresenter.handleAnswerLike(!answerInfoBean.getLiked(), answerInfoBean.getId(), answerInfoBean);
                    // 修改UI
                    dealLikeUI(answerInfoBean, tvLikeCount);
                });
        // 是否围观
        TextView tvToWatch = holder.getTextView(R.id.tv_to_watch);
        // 邀请的人回答才会有围观
        tvToWatch.setVisibility(canNotLook ? View.VISIBLE : View.GONE);
        // 是否已经围观了
        tvToWatch.setEnabled(!answerInfoBean.getCould());
        tvToWatch.setText(answerInfoBean.getCould() ? tvToWatch.getContext().getString(R.string.qa_go_to_watched)
                : tvToWatch.getContext().getString(R.string.qa_go_to_watch));
        tvToWatch.setTextColor(answerInfoBean.getCould() ? ContextCompat.getColor(tvToWatch.getContext(), R.color.normal_for_assist_text)
                : ContextCompat.getColor(tvToWatch.getContext(), R.color.white));
        RxView.clicks(tvToWatch)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null) {
                        mListener.onToWatchClick(answerInfoBean, position, canNotLook);
                    }
                });
        // 评论数量
        holder.setText(R.id.tv_comment_count, String.valueOf(answerInfoBean.getComments_count()));
    }

    private void dealLikeUI(AnswerInfoBean answerInfoBean, TextView tvLikeCount) {
        // 是否点赞
        Drawable unLike = UIUtils.getCompoundDrawables(tvLikeCount.getContext(), R.mipmap.home_ico_good_normal);
        Drawable liked = UIUtils.getCompoundDrawables(tvLikeCount.getContext(), R.mipmap.home_ico_good_high);
        tvLikeCount.setCompoundDrawables(answerInfoBean.getLiked() ? liked : unLike, null, null, null);
        // 回答数量
        tvLikeCount.setText(String.valueOf(answerInfoBean.getLikes_count()));
    }

    public void setOnGoToWatchClickListener(OnGoToWatchClickListener listener) {
        this.mListener = listener;
    }

    public void setOnSpanTextClickListener(TextViewUtils.OnSpanTextClickListener onSpanTextClickListener) {
        mOnSpanTextClickListener = onSpanTextClickListener;
    }

    public interface OnGoToWatchClickListener {
        void onToWatchClick(AnswerInfoBean answerInfoBean, int position, boolean isNeedOnlook);
    }
}
