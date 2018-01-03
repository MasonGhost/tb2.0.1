package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoLongClickListener;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/19
 * @contact email:648129313@qq.com
 */

public class GroupDynamicDetailCommentItem implements ItemViewDelegate<GroupDynamicCommentListBean> {
    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;
    private OnCommentTextClickListener mOnCommentTextClickListener;

    public void setOnCommentTextClickListener(OnCommentTextClickListener onCommentTextClickListener) {
        mOnCommentTextClickListener = onCommentTextClickListener;
    }

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(GroupDynamicCommentListBean item, int position) {
        return position == 0 || !TextUtils.isEmpty(item.getContent());
    }

    @Override
    public void convert(ViewHolder holder, GroupDynamicCommentListBean dynamicCommentBean, GroupDynamicCommentListBean lastT, final int position, int itemCounts) {
        holder.setText(R.id.tv_name, dynamicCommentBean.getCommentUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicCommentBean.getCreated_at()));
        holder.setText(R.id.tv_content, setShowText(dynamicCommentBean, position));
        holder.getView(R.id.tv_content).setOnClickListener(v -> {
            LogUtils.d("-----dy------onClick----------------");
            if (mOnCommentTextClickListener != null) {
                mOnCommentTextClickListener.onCommentTextClick(position);
            }
        });
        TextView topFlag = holder.getView(R.id.tv_top_flag);
        topFlag.setVisibility(/*dynamicCommentBean.getPinned() == 0 ? */View.GONE/* : View.VISIBLE*/);
        topFlag.setText(topFlag.getContext().getString(R.string.dynamic_top_flag));
        List<Link> links = setLiknks(holder, dynamicCommentBean, position);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }
        ImageUtils.loadCircleUserHeadPic(dynamicCommentBean.getCommentUser(), holder.getView(R.id.iv_headpic));
        setUserInfoClick(holder.getView(R.id.tv_name), dynamicCommentBean.getCommentUser());
        setUserInfoClick(holder.getView(R.id.iv_headpic), dynamicCommentBean.getCommentUser());
    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(aVoid -> {
            if (mOnUserInfoClickListener != null) {
                mOnUserInfoClickListener.onUserInfoClick(userInfoBean);
            }
        });
    }

    protected String setShowText(GroupDynamicCommentListBean dynamicCommentBean, int position) {
        return handleName(dynamicCommentBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final GroupDynamicCommentListBean dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (dynamicCommentBean.getReplyUser() != null && dynamicCommentBean.getReply_to_user_id() != 0 && dynamicCommentBean.getReplyUser().getName() != null) {
            Link replyNameLink = new Link(dynamicCommentBean.getReplyUser().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserInfoLongClickListener != null) {
                            mOnUserInfoLongClickListener.onUserInfoLongClick(dynamicCommentBean.getReplyUser());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        LogUtils.d("-----dy------setOnClickListener----------------");
                        // single clicked
                        if (mOnUserInfoClickListener != null) {
                            mOnUserInfoClickListener.onUserInfoClick(dynamicCommentBean.getReplyUser());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    /**
     * 处理名字的颜色与点击
     */
    private String handleName(GroupDynamicCommentListBean dynamicCommentBean) {
        String content = "";
        if (dynamicCommentBean.getReply_to_user_id() != 0) { // 当没有回复者时，就是回复评论
            content += "回复 " + dynamicCommentBean.getReplyUser().getName() + ": " + dynamicCommentBean.getContent();
        } else {
            content = dynamicCommentBean.getContent();
        }
        return content;
    }
}
