package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoLongClickListener;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicDetailCommentItem implements ItemViewDelegate<DynamicCommentBean> {
    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;
    private OnCommentTextClickListener mOnCommentTextClickListener;
    private OnCommentResendListener mOnCommentResendListener;

    public void setOnCommentTextClickListener(OnCommentTextClickListener onCommentTextClickListener) {
        mOnCommentTextClickListener = onCommentTextClickListener;
    }

    public void setOnCommentResendListener(OnCommentResendListener onCommentResendListener) {
        mOnCommentResendListener = onCommentResendListener;
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
    public boolean isForViewType(DynamicCommentBean item, int position) {
        return position == 0 || !TextUtils.isEmpty(item.getComment_content());
    }

    @Override
    public void convert(ViewHolder holder, DynamicCommentBean dynamicCommentBean, DynamicCommentBean lastT, final int position, int itemCounts) {
        holder.setText(R.id.tv_name, dynamicCommentBean.getCommentUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicCommentBean.getCreated_at()));
        holder.setText(R.id.tv_content, setShowText(dynamicCommentBean, position));
        holder.setVisible(R.id.fl_tip, dynamicCommentBean.getState() == DynamicCommentBean.SEND_ERROR ? View.VISIBLE : View.GONE);

        RxView.clicks(holder.getView(R.id.fl_tip))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mOnCommentResendListener != null) {
                        mOnCommentResendListener.reSendComment(dynamicCommentBean);
                    }
                });

        TextView topFlag = holder.getView(R.id.tv_top_flag);
        topFlag.setVisibility(!dynamicCommentBean.getPinned() ? View.GONE : View.VISIBLE);
        topFlag.setText(topFlag.getContext().getString(R.string.dynamic_top_flag));
        List<Link> links = setLiknks(holder, dynamicCommentBean, position);
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }
        holder.getView(R.id.tv_content).setOnClickListener(v -> {
            if (mOnCommentTextClickListener != null) {
                mOnCommentTextClickListener.onCommentTextClick(position);
            }
        });
        holder.getView(R.id.tv_content).setOnLongClickListener(v -> {
            if (mOnCommentTextClickListener != null) {
                mOnCommentTextClickListener.onCommentTextLongClick(position);
            }
            return true;
        });
        ImageUtils.loadCircleUserHeadPic(dynamicCommentBean.getCommentUser(), holder.getView(R.id.iv_headpic));
        //setUserInfoClick(holder.getView(R.id.tv_name), dynamicCommentBean.getCommentUser());
        //setUserInfoClick(holder.getView(R.id.iv_headpic), dynamicCommentBean.getCommentUser());
    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(aVoid -> {
            if (mOnUserInfoClickListener != null) {
                mOnUserInfoClickListener.onUserInfoClick(userInfoBean);
            }
        });
    }

    protected String setShowText(DynamicCommentBean dynamicCommentBean, int position) {
        return handleName(dynamicCommentBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final DynamicCommentBean dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        if (dynamicCommentBean.getReplyUser() != null && dynamicCommentBean.getReply_to_user_id() != 0 && dynamicCommentBean.getReplyUser().getName
                () != null) {
            Link replyNameLink = new Link(dynamicCommentBean.getReplyUser().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))
                    // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint))
                    // optional, defaults to .15f
                    .setHighlightAlpha(.5f)
                    .setBold(false)
                    // optional, defaults to true
                    .setUnderlined(false)
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
     *
     * @param dynamicCommentBean
     * @return
     */
    private String handleName(DynamicCommentBean dynamicCommentBean) {
        String content = "";
        if (dynamicCommentBean.getReply_to_user_id() != 0) { // 当没有回复者时，就是回复评论
            content += "回复 " + dynamicCommentBean.getReplyUser().getName() + ": " + dynamicCommentBean.getComment_content();
        } else {
            content = dynamicCommentBean.getComment_content();
        }
        return content;
    }

    public interface OnCommentResendListener {
        void reSendComment(DynamicCommentBean dynamicCommentBean);
    }
}
