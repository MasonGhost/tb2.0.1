package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.adapter;

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
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
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
public class PostDetailCommentItem implements ItemViewDelegate<CirclePostCommentBean> {

    private OnCommentItemListener mOnCommentItemListener;

    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    public PostDetailCommentItem(OnCommentItemListener onCommentItemListener) {
        mOnCommentItemListener = onCommentItemListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(CirclePostCommentBean item, int position) {
        return position == 0 || !TextUtils.isEmpty(item.getContent());
    }

    @Override
    public void convert(final ViewHolder holder, CirclePostCommentBean circlePostCommentBean,
                        CirclePostCommentBean lastT, final int position, int itemCounts) {

        ImageUtils.loadCircleUserHeadPic(circlePostCommentBean.getCommentUser(), holder.getView(R.id.iv_headpic));

        holder.getView(R.id.v_line).setVisibility(View.GONE);
        holder.setText(R.id.tv_name, circlePostCommentBean.getCommentUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(circlePostCommentBean
                .getCreated_at()));
        holder.setText(R.id.tv_content, setShowText(circlePostCommentBean, position));
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
        topFlag.setVisibility(!circlePostCommentBean.isPinned() ? View.GONE : View.VISIBLE);
        topFlag.setText(topFlag.getContext().getString(R.string.dynamic_top_flag));

        List<Link> links = setLiknks(holder, circlePostCommentBean, position);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onItemClick(v, holder, position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onItemLongClick(v, holder, position);
            }
            return true;
        });
        setUserInfoClick(holder.getView(R.id.tv_name), circlePostCommentBean.getCommentUser());
        setUserInfoClick(holder.getView(R.id.iv_headpic), circlePostCommentBean.getCommentUser());
    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(aVoid -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onUserInfoClick(userInfoBean);
            }
        });
    }

    protected String setShowText(CirclePostCommentBean circlePostCommentBean, int position) {
        return handleName(circlePostCommentBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final CirclePostCommentBean circlePostCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (circlePostCommentBean.getReplyUser() != null && circlePostCommentBean.getReply_to_user_id() != 0 && circlePostCommentBean.getReplyUser().getName() != null) {
            Link replyNameLink = new Link(circlePostCommentBean.getReplyUser().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserInfoLongClickListener != null) {
                            mOnUserInfoLongClickListener.onUserInfoLongClick(circlePostCommentBean.getReplyUser());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        if (mOnUserInfoClickListener != null) {
                            mOnUserInfoClickListener.onUserInfoClick(circlePostCommentBean.getReplyUser());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    private String handleName(CirclePostCommentBean circlePostCommentBean) {
        String content = "";
        // 当没有回复者时，就是回复评论
        if (circlePostCommentBean.getReply_to_user_id() != 0) {
            content += " 回复 " + circlePostCommentBean.getReplyUser().getName() + ": " +
                    circlePostCommentBean.getContent();
        } else {
            content = circlePostCommentBean.getContent();
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
