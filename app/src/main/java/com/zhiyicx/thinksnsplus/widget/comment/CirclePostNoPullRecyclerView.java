package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/11/30/14:20
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostNoPullRecyclerView extends CommentBaseRecycleView<CirclePostCommentBean> {

    private TopFlagPosition mTopFlagPosition = TopFlagPosition.NONE;

    public CirclePostNoPullRecyclerView(Context context) {
        super(context);
    }

    public CirclePostNoPullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CirclePostNoPullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void convertData(com.zhy.adapter.recyclerview.base.ViewHolder holder,
                               final CirclePostCommentBean dynamicCommentBean, final int position) {
        if (dynamicCommentBean.getState() == CirclePostCommentBean.SEND_ERROR) {
            holder.setVisible(com.zhiyicx.baseproject.R.id.iv_hint_img, VISIBLE);
        } else {
            holder.setVisible(com.zhiyicx.baseproject.R.id.iv_hint_img, INVISIBLE);
        }
        TextView contentTextView = holder.getView(com.zhiyicx.baseproject.R.id
                .tv_simple_text_comment);
        contentTextView.setText(setShowText(dynamicCommentBean, contentTextView));

        // Add the links and make the links clickable
        ConvertUtils.stringLinkConvert(contentTextView, setLiknks(dynamicCommentBean, position));

        holder.setOnClickListener(com.zhiyicx.baseproject.R.id.tv_simple_text_comment, v -> {
            if (mOnIitemClickListener != null) {
                mOnIitemClickListener.onItemClick(v, position);
            }
        });
        holder.setOnLongClickListener(com.zhiyicx.baseproject.R.id.tv_simple_text_comment, v -> {
            if (mOnIitemLongClickListener != null) {
                mOnIitemLongClickListener.onItemLongClick(v, position);
            }

            return false;
        });
        holder.setOnClickListener(com.zhiyicx.baseproject.R.id.iv_hint_img, v -> {
            if (mOnCommentStateClickListener != null) {
                mOnCommentStateClickListener.onCommentStateClick(dynamicCommentBean, position);
            }
        });
    }

    public void setTopFlagPosition(TopFlagPosition topFlagPosition) {
        mTopFlagPosition = topFlagPosition;
    }


    protected CharSequence setShowText(CirclePostCommentBean dynamicCommentBean, TextView contentTextView) {
        // 评论后加空格占位
        String content = handleName(dynamicCommentBean)+" ";
        // 不是置顶的评论则不用处理
        if (!dynamicCommentBean.getPinned()) {
            return content;
        } else if (mTopFlagPosition == TopFlagPosition.WORDS_RIGHT) {
            int lenght = content.length();
            Drawable top_drawable = ContextCompat.getDrawable(getContext(), R.mipmap.label_zhiding);
            top_drawable.setBounds(0, 0, (int) (contentTextView.getTextSize() * 2), contentTextView.getLineHeight());

            ImageSpan imgSpan = new CenterImageSpan(top_drawable);
            SpannableString spannableString = SpannableString.valueOf(content + "T");
            spannableString.setSpan(imgSpan, lenght, lenght + 1, Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
        return content;
    }

    protected List<Link> setLiknks(final CirclePostCommentBean dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (dynamicCommentBean.getCommentUser() != null) {
            Link commentNameLink = new Link(dynamicCommentBean.getCommentUser().getName())
                    .setTextColor(ContextCompat.getColor(getContext(), R.color
                            .important_for_content))

                    .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                            .general_for_hint))
                    .setHighlightAlpha(.8f)
                    .setUnderlined(false)
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserNameLongClickListener != null) {
                            mOnUserNameLongClickListener.onUserNameLongClick(dynamicCommentBean
                                    .getReplyUser());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        if (mOnUserNameClickListener != null) {
                            mOnUserNameClickListener.onUserNameClick(dynamicCommentBean
                                    .getCommentUser());
                        }
                    });
            links.add(commentNameLink);
        }

        if (dynamicCommentBean.getReply_to_user_id() != 0 && dynamicCommentBean.getReplyUser() !=
                null && dynamicCommentBean.getReplyUser().getName() != null) {
            Link replyNameLink = new Link(dynamicCommentBean.getReplyUser().getName())
                    .setTextColor(ContextCompat.getColor(getContext(), R.color
                            .important_for_content))
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                            .general_for_hint))
                    .setHighlightAlpha(.5f)
                    .setUnderlined(false)
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserNameLongClickListener != null) {
                            mOnUserNameLongClickListener.onUserNameLongClick(dynamicCommentBean
                                    .getReplyUser());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        if (mOnUserNameClickListener != null) {
                            mOnUserNameClickListener.onUserNameClick(dynamicCommentBean
                                    .getReplyUser());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    /**
     * 处理名字的颜色与点击
     *
     * @param dynamicCommentBean
     * @return
     */
    private String handleName(CirclePostCommentBean dynamicCommentBean) {
        String content = "";
        String comentUserName = dynamicCommentBean.getCommentUser() == null ? "该用户已被删除" :
                dynamicCommentBean.getCommentUser().getName();
        String replyUserName = dynamicCommentBean.getReplyUser() == null ? "该用户已被删除" :
                dynamicCommentBean.getReplyUser().getName();
        if (dynamicCommentBean.getReply_to_user_id() == 0) {
            // 当没有回复者时，就是回复评论
            content += "" + comentUserName + ":  " + dynamicCommentBean.getContent();
        } else {
            content += "" + comentUserName + " 回复 " + replyUserName + ":  " + dynamicCommentBean
                    .getContent();
        }
        return content;
    }
}
