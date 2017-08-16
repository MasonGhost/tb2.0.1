package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Catherine
 * @describe 答案列表item
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class AnswerListItem implements ItemViewDelegate<AnswerInfoBean> {

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
        if (answerInfoBean.getUser() != null){
            ImageUtils.loadCircleUserHeadPic(answerInfoBean.getUser(), holder.getView(R.id.iv_portrait));
            holder.setText(R.id.tv_name, answerInfoBean.getUser().getName());
            // 围观数量 PS：围观是只有邀请了专家来回答的才有哦
            boolean isInvited = answerInfoBean.getInvited() == 1;
            holder.setVisible(R.id.tv_watcher_count, isInvited ? View.VISIBLE : View.GONE);
            if (isInvited){
                holder.setText(R.id.tv_watcher_count, String.format(holder.getConvertView().getContext()
                        .getString(R.string.qa_question_answer_show_watcher_count), answerInfoBean.getRewarder_count()));
            }
        } else if (answerInfoBean.getAnonymity() == 1){
            // 为空 应该就是匿名了
            holder.setVisible(R.id.iv_anonymity_flag, View.VISIBLE);
            holder.setVisible(R.id.iv_portrait, View.GONE);
        }
        // 是否采纳
        holder.setVisible(R.id.tv_adopt_flag, answerInfoBean.getAdoption() == 1 ? View.VISIBLE : View.GONE);
        // 正文
        holder.setText(R.id.tv_content, answerInfoBean.getBody());
        // 点赞数量
        TextView tvLikeCount = holder.getView(R.id.tv_like_count);
        tvLikeCount.setText(String.valueOf(answerInfoBean.getLikes_count()));
        // 是否点赞
        Drawable unLike = UIUtils.getCompoundDrawables(tvLikeCount.getContext(), R.mipmap.home_ico_good_normal);
        Drawable liked = UIUtils.getCompoundDrawables(tvLikeCount.getContext(), R.mipmap.home_ico_good_high);
        tvLikeCount.setCompoundDrawables(answerInfoBean.getLiked() ? liked : unLike, null, null, null);
        // 回答数量
        holder.setText(R.id.tv_comment_count, String.valueOf(answerInfoBean.getComments_count()));
    }
}
