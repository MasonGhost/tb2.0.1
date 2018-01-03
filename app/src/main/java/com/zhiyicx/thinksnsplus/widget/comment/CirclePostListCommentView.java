package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/11/30/14:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostListCommentView extends LinearLayout {

    private static final int SHOW_MORE_COMMENT_SIZE_LIMIT = 6;

    private CirclePostNoPullRecyclerView mCirclePostNoPullRecyclerView;
    private TextView mMoreComment;


    private OnMoreCommentClickListener mOnMoreCommentClickListener;
    private OnCommentClickListener mOnCommentClickListener;
    private CirclePostNoPullRecyclerView.OnCommentStateClickListener mOnCommentStateClickListener;
    private CirclePostListBean mDynamicBean;

    private boolean mIsUserNameClick = false; // 标识用户名被点击还是评论被点击了

    public CirclePostListCommentView(Context context) {
        super(context);
        init();
    }

    public CirclePostListCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CirclePostListCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_circle_post_list_comment, this);
        mCirclePostNoPullRecyclerView = (CirclePostNoPullRecyclerView) findViewById(R.id.fl_comment);
        mMoreComment = (TextView) findViewById(R.id.tv_more_comment);
        setListener();
    }

    private void setListener() {
        RxView.clicks(mMoreComment)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnMoreCommentClickListener != null) {
                        mOnMoreCommentClickListener.onMoreCommentClick(mMoreComment, mDynamicBean);
                    }
                });
        mCirclePostNoPullRecyclerView.setOnUserNameClickListener(userInfoBean -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentUserInfoClick(userInfoBean);
                    mIsUserNameClick = true;
                }
            }
        });
        mCirclePostNoPullRecyclerView.setOnIitemClickListener((view, position) -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentContentClick(mDynamicBean, position);
                }
            } else {
                mIsUserNameClick = false;

            }
        });
        mCirclePostNoPullRecyclerView.setOnIitemLongClickListener((view, position) -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentContentLongClick(mDynamicBean, position);
                }
            } else {
                mIsUserNameClick = false;

            }
        });
        mCirclePostNoPullRecyclerView.setOnUserNameLongClickListener(userInfoBean -> {
            if (!mIsUserNameClick) {
                mIsUserNameClick = true;
            }
        });
        setOnClickListener(v -> {
        });
        mCirclePostNoPullRecyclerView.setOnCommentStateClickListener((dynamicCommentBean, position) -> {
            if (mOnCommentStateClickListener != null) {
                mOnCommentStateClickListener.onCommentStateClick(dynamicCommentBean, position);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param dynamicBean
     */
    public void setData(CirclePostListBean dynamicBean) {
        mDynamicBean = dynamicBean;
        List<CirclePostCommentBean> data = new ArrayList<>();

        if (dynamicBean.getComments() != null && !dynamicBean.getComments().isEmpty()) {
            //最多显示5条
            if (dynamicBean.getComments().size() >= SHOW_MORE_COMMENT_SIZE_LIMIT) {
                for (int i = 0; i < SHOW_MORE_COMMENT_SIZE_LIMIT - 1; i++) {
                    data.add(dynamicBean.getComments().get(i));
                }
            } else {
                data.addAll(dynamicBean.getComments());
            }
        }
        mCirclePostNoPullRecyclerView.setTopFlagPosition(CommentBaseRecycleView.TopFlagPosition.WORDS_RIGHT);
        mCirclePostNoPullRecyclerView.setData(data);
        if (dynamicBean.getComments_count() >= SHOW_MORE_COMMENT_SIZE_LIMIT) {
            mMoreComment.setVisibility(VISIBLE);
        } else {
            mMoreComment.setVisibility(GONE);
        }
    }

    public void setOnMoreCommentClickListener(OnMoreCommentClickListener onMoreCommentClickListener) {
        mOnMoreCommentClickListener = onMoreCommentClickListener;
    }

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    public void setOnCommentStateClickListener(CirclePostNoPullRecyclerView.OnCommentStateClickListener onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    public interface OnMoreCommentClickListener {
        void onMoreCommentClick(View view, CirclePostListBean dynamicBean);
    }

    public interface OnCommentClickListener {
        void onCommentUserInfoClick(UserInfoBean userInfoBean);

        void onCommentContentClick(CirclePostListBean dynamicBean, int position);

        void onCommentContentLongClick(CirclePostListBean dynamicBean, int position);
    }
}
