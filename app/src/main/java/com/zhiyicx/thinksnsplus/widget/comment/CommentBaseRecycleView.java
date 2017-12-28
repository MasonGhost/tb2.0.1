package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Author Jliuer
 * @Date 2017/07/19/11:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CommentBaseRecycleView<D> extends SimpleTextNoPullRecycleView<D> {

    protected OnUserNameClickListener mOnUserNameClickListener;
    protected OnUserNameLongClickListener mOnUserNameLongClickListener;
    protected OnCommentStateClickListener<D> mOnCommentStateClickListener;

    public CommentBaseRecycleView(Context context) {
        super(context);
    }

    public CommentBaseRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentBaseRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void convertData(com.zhy.adapter.recyclerview.base.ViewHolder holder, D d, int position) {

    }

    public OnUserNameClickListener getOnUserNameClickListener() {
        return mOnUserNameClickListener;
    }

    public void setOnUserNameClickListener(OnUserNameClickListener onUserNameClickListener) {
        mOnUserNameClickListener = onUserNameClickListener;
    }

    public OnUserNameLongClickListener getOnUserNameLongClickListener() {
        return mOnUserNameLongClickListener;
    }

    public void setOnUserNameLongClickListener(OnUserNameLongClickListener onUserNameLongClickListener) {
        mOnUserNameLongClickListener = onUserNameLongClickListener;
    }

    public OnCommentStateClickListener getOnCommentStateClickListener() {
        return mOnCommentStateClickListener;
    }

    public void setOnCommentStateClickListener(OnCommentStateClickListener<D> onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    public interface OnUserNameClickListener {
        void onUserNameClick(UserInfoBean userInfoBean);

    }

    public interface OnUserNameLongClickListener {
        void onUserNameLongClick(UserInfoBean userInfoBean);

    }

    public interface OnCommentStateClickListener<D> {
        void onCommentStateClick(D dynamicCommentBean, int position);
    }

    public enum TopFlagPosition {
        VIEW_RIGHT("在整个 view 的右边，居中对齐"),
        WORDS_RIGHT("文字末尾的右边，与最后一排文字居中对齐"),
        NONE("无置顶标记");

        TopFlagPosition(String desc) {
        }
    }
}
