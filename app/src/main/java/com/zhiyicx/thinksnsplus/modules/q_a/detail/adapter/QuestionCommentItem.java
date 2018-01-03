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
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoLongClickListener;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public class QuestionCommentItem implements ItemViewDelegate<QuestionCommentBean> {


    private OnCommentItemListener mOnCommentItemListener;

    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    public QuestionCommentItem(OnCommentItemListener onCommentItemListener) {
        mOnCommentItemListener = onCommentItemListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(QuestionCommentBean item, int position) {
        return !TextUtils.isEmpty(item.getBody());
    }

    @Override
    public void convert(ViewHolder holder, QuestionCommentBean questionCommentBean, QuestionCommentBean lastT, int position, int itemCounts) {
        ImageUtils.loadCircleUserHeadPic(questionCommentBean.getFromUserInfoBean(), holder.getView(R.id.iv_headpic));

        holder.setText(R.id.tv_name, questionCommentBean.getFromUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(questionCommentBean
                .getCreated_at()));
        holder.setText(R.id.tv_content, setShowText(questionCommentBean, position));
        holder.setOnClickListener(R.id.tv_content, v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onCommentTextClick(v, holder, position);
            }
        });
        holder.setOnLongClickListener(R.id.tv_content, v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onCommentTextLongClick(v, holder, position);
            }
            return true;
        });

        TextView topFlag = holder.getView(R.id.tv_top_flag);
        topFlag.setVisibility(View.GONE);
        topFlag.setText(topFlag.getContext().getString(R.string.dynamic_top_flag));

        List<Link> links = setLinks(holder, questionCommentBean, position);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onCommentTextClick(v, holder, position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onCommentTextLongClick(v, holder, position);
            }
            return true;
        });
        setUserInfoClick(holder.getView(R.id.tv_name), questionCommentBean.getFromUserInfoBean());
        setUserInfoClick(holder.getView(R.id.iv_headpic), questionCommentBean.getFromUserInfoBean());
    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(aVoid -> {
            if (mOnCommentItemListener != null) {
                mOnCommentItemListener.onUserInfoClick(userInfoBean);
            }
        });
    }

    protected String setShowText(QuestionCommentBean questionCommentBean, int position) {
        return handleName(questionCommentBean);
    }

    protected List<Link> setLinks(ViewHolder holder, final QuestionCommentBean questionCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (questionCommentBean.getToUserInfoBean() != null && questionCommentBean.getReply_user() != 0 && questionCommentBean.getToUserInfoBean()
                .getName() != null) {
            Link replyNameLink = new Link(questionCommentBean.getToUserInfoBean().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  //
                    // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) //
                    // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserInfoLongClickListener != null) {
                            mOnUserInfoLongClickListener.onUserInfoLongClick(questionCommentBean.getToUserInfoBean());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        if (mOnUserInfoClickListener != null) {
                            mOnUserInfoClickListener.onUserInfoClick(questionCommentBean.getToUserInfoBean());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    private String handleName(QuestionCommentBean questionCommentBean) {
        String content = "";
        if (questionCommentBean.getReply_user() != 0) { // 当没有回复者时，就是回复评论
            content += " 回复 " + questionCommentBean.getToUserInfoBean().getName() + ": " +
                    questionCommentBean.getBody();
        } else {
            content = questionCommentBean.getBody();
        }
        return content;
    }

    public interface OnCommentItemListener {
        void onCommentTextClick(View view, RecyclerView.ViewHolder holder, int position);

        void onCommentTextLongClick(View view, RecyclerView.ViewHolder holder, int position);

        void onUserInfoClick(UserInfoBean userInfoBean);
    }
}
