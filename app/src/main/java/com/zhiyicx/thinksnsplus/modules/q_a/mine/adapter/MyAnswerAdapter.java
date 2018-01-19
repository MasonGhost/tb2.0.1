package com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyAnswerAdapter extends CommonAdapter<AnswerInfoBean> {

    protected MyAnswerContract.Presenter mPresenter;

    public MyAnswerAdapter(Context context, List<AnswerInfoBean> data, MyAnswerContract.Presenter presenter) {
        super(context, R.layout.item_question_answer, data);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, AnswerInfoBean answerInfoBean, int position) {
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
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(holder.getConvertView().getContext(), answerInfoBean.getUser()));
        }
        // 是否采纳
        holder.setVisible(R.id.tv_adopt_flag, View.GONE);
        // 是否邀请
        holder.setVisible(R.id.tv_invite_flag, View.GONE);
        // 是否围观
        holder.setVisible(R.id.tv_to_watch, View.GONE);
        // 时间
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(answerInfoBean.getCreated_at()));
        // 正文
        holder.setText(R.id.tv_content, RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, answerInfoBean.getBody()));
        // 点赞数量
        TextView tvLikeCount = holder.getView(R.id.tv_like_count);
        dealLikeUI(answerInfoBean, tvLikeCount);
        RxView.clicks(tvLikeCount)
                .filter(aVoid -> mPresenter != null)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> mPresenter.handleLike(position, answerInfoBean));
        // 评论数量
        holder.setText(R.id.tv_comment_count, String.valueOf(answerInfoBean.getComments_count()));
    }

    protected void dealLikeUI(AnswerInfoBean answerInfoBean, TextView tvLikeCount) {
        // 是否点赞
        Drawable unLike = UIUtils.getCompoundDrawables(tvLikeCount.getContext(), R.mipmap.home_ico_good_normal);
        Drawable liked = UIUtils.getCompoundDrawables(tvLikeCount.getContext(), R.mipmap.home_ico_good_high);
        tvLikeCount.setCompoundDrawables(answerInfoBean.getLiked() ? liked : unLike, null, null, null);
        // 回答数量
        tvLikeCount.setText(String.valueOf(answerInfoBean.getLikes_count()));
    }

    public void setPresenter(MyAnswerContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
