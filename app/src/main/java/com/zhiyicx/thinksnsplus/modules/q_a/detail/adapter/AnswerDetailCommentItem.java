package com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoLongClickListener;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerDetailCommentItem implements ItemViewDelegate<AnswerCommentListBean> {

    private OnCommentItemListener mOnCommentItemListener;

    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    public AnswerDetailCommentItem(OnCommentItemListener onCommentItemListener) {
        mOnCommentItemListener = onCommentItemListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(AnswerCommentListBean item, int position) {
        return !TextUtils.isEmpty(item.getBody());
    }

    @Override
    public void convert(final ViewHolder holder, AnswerCommentListBean infoCommentListBean,
                        AnswerCommentListBean lastT, final int position, int itemCounts) {

        ImageUtils.loadCircleUserHeadPic(infoCommentListBean.getFromUserInfoBean(), holder.getView(R.id.iv_headpic));

        holder.setText(R.id.tv_name, infoCommentListBean.getFromUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(infoCommentListBean
                .getCreated_at()));
        holder.setText(R.id.tv_content, setShowText(infoCommentListBean, position));
              holder.setOnClickListener(R.id.tv_content, v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onItemClick(v, holder, position);
            }
        });
        holder.setOnLongClickListener(R.id.tv_content, v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onItemLongClick(v, holder, position);
            }
            return true;
        });

        TextView topFlag = holder.getView(R.id.tv_top_flag);
        topFlag.setVisibility(View.GONE);
        topFlag.setText(topFlag.getContext().getString(R.string.dynamic_top_flag));

        List<Link> links = setLiknks(holder, infoCommentListBean, position);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onItemClick(v, holder, position);
            }
        });
        setUserInfoClick(holder.getView(R.id.tv_name), infoCommentListBean.getFromUserInfoBean());
        setUserInfoClick(holder.getView(R.id.iv_headpic), infoCommentListBean.getFromUserInfoBean());
    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(aVoid -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onUserInfoClick(userInfoBean);
            }
        });
    }

    protected String setShowText(AnswerCommentListBean answerCommentListBean, int position) {
        return handleName(answerCommentListBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final AnswerCommentListBean answerCommentListBean, int position) {
        List<Link> links = new ArrayList<>();
        if (answerCommentListBean.getToUserInfoBean() != null && answerCommentListBean.getReply_user() != 0 && answerCommentListBean
                .getToUserInfoBean().getName() != null) {
            Link replyNameLink = new Link(answerCommentListBean.getToUserInfoBean().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  //
                    // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) //
                    // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserInfoLongClickListener != null) {
                            mOnUserInfoLongClickListener.onUserInfoLongClick(answerCommentListBean.getToUserInfoBean());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        if (mOnUserInfoClickListener != null) {
                            mOnUserInfoClickListener.onUserInfoClick(answerCommentListBean.getToUserInfoBean());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    private String handleName(AnswerCommentListBean answerCommentListBean) {
        String content = "";
        if (answerCommentListBean.getReply_user() != 0) { // 当没有回复者时，就是回复评论
            content += " 回复 " + answerCommentListBean.getToUserInfoBean().getName() + ": " +
                    answerCommentListBean.getBody();
        } else {
            content = answerCommentListBean.getBody();
        }
        return content;
    }

    public void setOnCommentItemListener(OnCommentItemListener onCommentItemListener) {
        mOnCommentItemListener = onCommentItemListener;
    }

    public interface OnCommentItemListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);

        void onUserInfoClick(UserInfoBean userInfoBean);
    }
}
