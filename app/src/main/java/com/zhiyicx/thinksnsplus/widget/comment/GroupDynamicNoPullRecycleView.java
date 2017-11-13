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
import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe dynamic list comment view
 * @Author Jungle68
 * @Date 2017/3/6
 * @Contact master.jungle68@gmail.com
 */
public class GroupDynamicNoPullRecycleView extends CommentBaseRecycleView<GroupDynamicCommentListBean> {

    public GroupDynamicNoPullRecycleView(Context context) {
        super(context);
    }

    public GroupDynamicNoPullRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupDynamicNoPullRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void convertData(com.zhy.adapter.recyclerview.base.ViewHolder holder,
                               final GroupDynamicCommentListBean dynamicCommentBean, final int position) {
        if (dynamicCommentBean.getState() == DynamicCommentBean.SEND_ERROR) {
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


    protected CharSequence setShowText(GroupDynamicCommentListBean dynamicCommentBean, TextView contentTextView) {
        return handleName(dynamicCommentBean);
    }

    protected List<Link> setLiknks(final GroupDynamicCommentListBean dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (dynamicCommentBean.getCommentUser() != null) {// 我也不知道这个怎么会是 null
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
    private String handleName(GroupDynamicCommentListBean dynamicCommentBean) {
        String content = "";
        String comentUserName = dynamicCommentBean.getCommentUser() == null ? "该用户已被删除" :
                dynamicCommentBean.getCommentUser().getName();
        String replyUserName = dynamicCommentBean.getReplyUser() == null ? "该用户已被删除" :
                dynamicCommentBean.getReplyUser().getName();
        if (dynamicCommentBean.getReply_to_user_id() == 0) { // 当没有回复者时，就是回复评论

            content += "" + comentUserName + ":  " + dynamicCommentBean.getContent();
        } else {
            content += "" + comentUserName + " 回复 " + replyUserName + ":  " + dynamicCommentBean
                    .getContent();
        }
        return content;
    }

}
