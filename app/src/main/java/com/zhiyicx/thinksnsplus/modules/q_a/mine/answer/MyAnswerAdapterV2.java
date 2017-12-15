package com.zhiyicx.thinksnsplus.modules.q_a.mine.answer;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.adapter.MyAnswerAdapter;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/11/23/10:17
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MyAnswerAdapterV2 extends MyAnswerAdapter {

    public MyAnswerAdapterV2(Context context, List<AnswerInfoBean> data, MyAnswerContract.Presenter presenter) {
        super(context, data, presenter);
    }

    @Override
    protected void convert(ViewHolder holder, AnswerInfoBean answerInfoBean, int position) {

        // 隐藏头像
        holder.setVisible(R.id.iv_portrait, View.GONE);
        // 隐藏名字
        holder.setVisible(R.id.tv_name, View.GONE);
        // 隐藏时间
        holder.setVisible(R.id.tv_time, View.GONE);
        // 是否采纳
        holder.setVisible(R.id.tv_adopt_flag, View.GONE);
        // 是否邀请
        holder.setVisible(R.id.tv_invite_flag, View.GONE);
        // 是否围观
        holder.setVisible(R.id.tv_to_watch, View.GONE);

        // 时间
        holder.setText(R.id.tv_watcher_count, TimeUtils.getTimeFriendlyNormal(answerInfoBean.getCreated_at()));
        // 正文
        holder.setText(R.id.tv_content, RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, answerInfoBean.getBody()));
        // 点赞数量
        TextView tvLikeCount = holder.getView(R.id.tv_like_count);
        dealLikeUI(answerInfoBean, tvLikeCount);
        RxView.clicks(tvLikeCount)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe((Void aVoid) -> mPresenter.handleLike(position, answerInfoBean));
        // 评论数量
        holder.setText(R.id.tv_comment_count, String.valueOf(answerInfoBean.getComments_count()));

        ((LinearLayout.LayoutParams) holder.getView(R.id.ll_tool_container).getLayoutParams()).setMargins(holder.getConvertView().getResources()
                .getDimensionPixelOffset
                (R.dimen.spacing_normal), 0, 0, 0);
    }
}
