package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
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
    protected String setShowText(DynamicCommentBean dynamicCommentBean, int position) {
        return handleName(dynamicCommentBean);
    }

    @Override
    protected List<Link> setLiknks(final DynamicCommentBean dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        Link commentNameLink = new Link(dynamicCommentBean.getCommentUser().getName())
                .setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.8f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {
                        if (mOnUserNameLongClickListener != null) {
                            mOnUserNameLongClickListener.onUserNameLongClick(dynamicCommentBean.getReplyUser());
                        }
                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        // single clicked
                        if (mOnUserNameClickListener != null) {
                            mOnUserNameClickListener.onUserNameClick(dynamicCommentBean.getCommentUser());
                        }
                    }
                });
        Link replyNameLink = new Link(dynamicCommentBean.getReplyUser().getName())
                .setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {
                        if (mOnUserNameLongClickListener != null) {
                            mOnUserNameLongClickListener.onUserNameLongClick(dynamicCommentBean.getReplyUser());
                        }
                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        // single clicked
                        if (mOnUserNameClickListener != null) {
                            mOnUserNameClickListener.onUserNameClick(dynamicCommentBean.getReplyUser());
                        }
                    }
                });
        links.add(commentNameLink);
        links.add(replyNameLink);
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
        if (dynamicCommentBean.getReplyUser().getUser_id().longValue() == dynamicCommentBean.getFeed_user_id()) {
            content += "" + dynamicCommentBean.getCommentUser().getName() + ":  " + dynamicCommentBean.getComment_content();
        } else {
            content += "" + dynamicCommentBean.getCommentUser().getName() + " 回复 " + dynamicCommentBean.getReplyUser().getName() + ":  " + dynamicCommentBean.getComment_content();
        }
        return content;
    }


    public void setOnUserNameClickListener(OnUserNameClickListener onUserNameClickListener) {
        mOnUserNameClickListener = onUserNameClickListener;
    }

    public void setOnUserNameLongClickListener(OnUserNameLongClickListener onUserNameLongClickListener) {
        mOnUserNameLongClickListener = onUserNameLongClickListener;
    }

    public interface OnUserNameClickListener {
        void onUserNameClick(UserInfoBean userInfoBean);

    }

    public interface OnUserNameLongClickListener {
        void onUserNameLongClick(UserInfoBean userInfoBean);

    }
}
