package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe dynamic list comment view
 * @Author Jungle68
 * @Date 2017/3/6
 * @Contact master.jungle68@gmail.com
 */
public class DynamicNoPullRecycleView extends SimpleTextNoPullRecycleView<DynamicCommentBean> {

    private OnUserNameClickListener mOnUserNameClickListener;
    private OnUserNameLongClickListener mOnUserNameLongClickListener;
    private OnCommentStateClickListener mOnCommentStateClickListener;

    public DynamicNoPullRecycleView(Context context) {
        super(context);
    }

    public DynamicNoPullRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicNoPullRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void convertData(com.zhy.adapter.recyclerview.base.ViewHolder holder, final DynamicCommentBean dynamicCommentBean, final int position) {
        if (dynamicCommentBean.getState() == DynamicCommentBean.SEND_ERROR) {
            holder.setVisible(com.zhiyicx.baseproject.R.id.iv_hint_img, VISIBLE);
        } else {
            holder.setVisible(com.zhiyicx.baseproject.R.id.iv_hint_img, INVISIBLE);
        }
        TextView contentTextView = holder.getView(com.zhiyicx.baseproject.R.id.tv_simple_text_comment);
        holder.setText(com.zhiyicx.baseproject.R.id.tv_simple_text_comment, setShowText(dynamicCommentBean, position));
        // Add the links and make the links clickable
        ConvertUtils.stringLinkConvert(contentTextView, setLiknks(dynamicCommentBean, position));
        Drawable top_drawable = contentTextView.getResources().getDrawable(R.mipmap.label_zhiding);
        contentTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                dynamicCommentBean.getPinned() == 1 ? top_drawable : null, null);
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

    protected String setShowText(DynamicCommentBean dynamicCommentBean, int position) {
        return handleName(dynamicCommentBean);
    }

    protected List<Link> setLiknks(final DynamicCommentBean dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (dynamicCommentBean.getCommentUser() != null) {// 我也不知道这个怎么会是 null
            Link commentNameLink = new Link(dynamicCommentBean.getCommentUser().getName())
                    .setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.8f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener(clickedText -> {
                        if (mOnUserNameLongClickListener != null) {
                            mOnUserNameLongClickListener.onUserNameLongClick(dynamicCommentBean.getReplyUser());
                        }
                    })
                    .setOnClickListener(clickedText -> {
                        // single clicked
                        if (mOnUserNameClickListener != null) {
                            mOnUserNameClickListener.onUserNameClick(dynamicCommentBean.getCommentUser());
                        }
                    });
            links.add(commentNameLink);
        }

        if (dynamicCommentBean.getReply_to_user_id() != 0 && dynamicCommentBean.getReplyUser() != null && dynamicCommentBean.getReplyUser().getName() != null) {
            Link replyNameLink = new Link(dynamicCommentBean.getReplyUser().getName())
                    .setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener(clickedText -> {
                        if (mOnUserNameLongClickListener != null) {
                            mOnUserNameLongClickListener.onUserNameLongClick(dynamicCommentBean.getReplyUser());
                        }
                    })
                    .setOnClickListener(clickedText -> {
                        // single clicked
                        if (mOnUserNameClickListener != null) {
                            mOnUserNameClickListener.onUserNameClick(dynamicCommentBean.getReplyUser());
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
    private String handleName(DynamicCommentBean dynamicCommentBean) {
        String content = "";
        String comentUserName = dynamicCommentBean.getCommentUser() == null ? "该用户已被删除" : dynamicCommentBean.getCommentUser().getName();
        String replyUserName = dynamicCommentBean.getReplyUser() == null ? "该用户已被删除" : dynamicCommentBean.getReplyUser().getName();
        if (dynamicCommentBean.getReply_to_user_id() == 0) { // 当没有回复者时，就是回复评论

            content += "" + comentUserName + ":  " + dynamicCommentBean.getComment_content();
        } else {
            content += "" + comentUserName + " 回复 " + replyUserName + ":  " + dynamicCommentBean.getComment_content();
        }
        return content;
    }


    public void setOnUserNameClickListener(OnUserNameClickListener onUserNameClickListener) {
        mOnUserNameClickListener = onUserNameClickListener;
    }

    public void setOnUserNameLongClickListener(OnUserNameLongClickListener onUserNameLongClickListener) {
        mOnUserNameLongClickListener = onUserNameLongClickListener;
    }

    public void setOnCommentStateClickListener(OnCommentStateClickListener onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    public interface OnUserNameClickListener {
        void onUserNameClick(UserInfoBean userInfoBean);

    }

    public interface OnUserNameLongClickListener {
        void onUserNameLongClick(UserInfoBean userInfoBean);

    }

    public interface OnCommentStateClickListener {
        void onCommentStateClick(DynamicCommentBean dynamicCommentBean, int position);
    }

}
