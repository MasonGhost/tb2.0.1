package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailContract;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailPresenter;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

    public AnswerListItem(QuestionDetailContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_question_answer;
    }

    @Override
    public boolean isForViewType(AnswerInfoBean item, int position) {
        return !TextUtils.isEmpty(item.getBody());
    }

    @Override
    public void convert(ViewHolder holder, AnswerInfoBean answerInfoBean, AnswerInfoBean lastT, int position, int itemCounts) {
        // 发布者信息
        if (answerInfoBean.getUser() != null) {
            ImageUtils.loadCircleUserHeadPic(answerInfoBean.getUser(), holder.getView(R.id.iv_portrait));
            holder.setText(R.id.tv_name, answerInfoBean.getUser().getName());
            // 围观数量 PS：围观是只有邀请了专家来回答的才有哦
            boolean isInvited = answerInfoBean.getInvited() == 1;
            holder.setVisible(R.id.tv_watcher_count, isInvited ? View.VISIBLE : View.GONE);
            if (isInvited) {
                holder.setText(R.id.tv_watcher_count, String.format(holder.getConvertView().getContext()
                        .getString(R.string.qa_question_answer_show_watcher_count), answerInfoBean.getOnlookers_count()));
            }
            // 跳转
            RxView.clicks(holder.getView(R.id.iv_portrait))
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        PersonalCenterFragment.startToPersonalCenter(holder.getConvertView().getContext(), answerInfoBean.getUser());
                    });
        } else if (answerInfoBean.getAnonymity() == 1) {
            // 为空 应该就是匿名了
            holder.setVisible(R.id.iv_anonymity_flag, View.VISIBLE);
            holder.setVisible(R.id.iv_portrait, View.GONE);
        }
        // 是否采纳
        holder.setVisible(R.id.tv_adopt_flag, answerInfoBean.getAdoption() == 1 ? View.VISIBLE : View.GONE);
        // 是否邀请
        holder.setVisible(R.id.tv_invite_flag, answerInfoBean.getInvited() == 1 ? View.VISIBLE : View.GONE);
        // 时间
        holder.setText(R.id.tv_time, answerInfoBean.getCreated_at());
        // 正文
        holder.setText(R.id.tv_content, RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, answerInfoBean.getBody()));
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
        tvToWatch.setVisibility(answerInfoBean.getInvited() == 1 ? View.VISIBLE : View.GONE);
        // 是否已经围观了
        tvToWatch.setEnabled(!answerInfoBean.getCould());
        tvToWatch.setText(answerInfoBean.getCould() ? tvToWatch.getContext().getString(R.string.qa_go_to_watched)
                : tvToWatch.getContext().getString(R.string.qa_go_to_watch));
        tvToWatch.setTextColor(answerInfoBean.getCould() ? ContextCompat.getColor(tvToWatch.getContext(), R.color.normal_for_assist_text)
                : ContextCompat.getColor(tvToWatch.getContext(), R.color.white));
        RxView.clicks(tvToWatch)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mListener != null){
                        mListener.onToWatchClick(answerInfoBean, position);
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

    public void setOnGoToWatchClickListener(OnGoToWatchClickListener listener){
        this.mListener = listener;
    }

    public interface OnGoToWatchClickListener{
        void onToWatchClick(AnswerInfoBean answerInfoBean, int position);
    }
}
