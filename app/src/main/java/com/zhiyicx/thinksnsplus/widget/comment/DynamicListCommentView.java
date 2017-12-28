package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.TimeStringSortClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表 评论控件
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListCommentView extends LinearLayout {
    public static final int SHOW_MORE_COMMENT_SIZE_LIMIT = 6;

    private DynamicNoPullRecycleView mDynamicNoPullRecycleView;
    private TextView mMoreComment;


    private OnMoreCommentClickListener mOnMoreCommentClickListener;
    private OnCommentClickListener mOnCommentClickListener;
    private DynamicNoPullRecycleView.OnCommentStateClickListener mOnCommentStateClickListener;
    private DynamicDetailBeanV2 mDynamicBean;
    /**
     * 标识用户名被点击还是评论被点击了
     */
    private boolean mIsUserNameClick = false;

    public DynamicListCommentView(Context context) {
        super(context);
        init();
    }

    public DynamicListCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicListCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_dynamic_list_comment, this);
        mDynamicNoPullRecycleView = (DynamicNoPullRecycleView) findViewById(R.id.fl_comment);
        mMoreComment = (TextView) findViewById(R.id.tv_more_comment);
        setListener();
    }

    private void setListener() {
        RxView.clicks(mMoreComment)
                // JITTER_SPACING_TIME秒钟之内只取一个点击事件，防抖操作
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnMoreCommentClickListener != null) {
                        mOnMoreCommentClickListener.onMoreCommentClick(mMoreComment, mDynamicBean);
                    }
                });
        mDynamicNoPullRecycleView.setOnUserNameClickListener(userInfoBean -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentUserInfoClick(userInfoBean);
                    mIsUserNameClick = true;
                }
            }
        });
        mDynamicNoPullRecycleView.setOnIitemClickListener((view, position) -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentContentClick(mDynamicBean, position);
                }
            } else {
                mIsUserNameClick = false;

            }
        });
        mDynamicNoPullRecycleView.setOnIitemLongClickListener((view, position) -> {
            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentContentLongClick(mDynamicBean, position);
                }
            } else {
                mIsUserNameClick = false;

            }

        });
        mDynamicNoPullRecycleView.setOnUserNameLongClickListener(userInfoBean -> {
            if (!mIsUserNameClick) {
                mIsUserNameClick = true;
            }
        });
        setOnClickListener(v -> {
        });
        mDynamicNoPullRecycleView.setOnCommentStateClickListener((dynamicCommentBean, position) -> {
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
    public void setData(DynamicDetailBeanV2 dynamicBean) {
        mDynamicBean = dynamicBean;
        List<DynamicCommentBean> data = new ArrayList<>();

        if (dynamicBean.getComments() != null && !dynamicBean.getComments().isEmpty()) {
            //最多显示 SHOW_MORE_COMMENT_SIZE_LIMIT-1 条
            if (dynamicBean.getComments().size() >= SHOW_MORE_COMMENT_SIZE_LIMIT) {
                for (int i = 0; i < SHOW_MORE_COMMENT_SIZE_LIMIT - 1; i++) {
                    data.add(dynamicBean.getComments().get(i));
                }
            } else {
                data.addAll(dynamicBean.getComments());
            }
        }
        mDynamicNoPullRecycleView.setTopFlagPosition(CommentBaseRecycleView.TopFlagPosition.WORDS_RIGHT);
        mDynamicNoPullRecycleView.setData(data);
        if (dynamicBean.getFeed_comment_count() >= SHOW_MORE_COMMENT_SIZE_LIMIT) {
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

    public void setOnCommentStateClickListener(DynamicNoPullRecycleView.OnCommentStateClickListener onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    public interface OnMoreCommentClickListener {
        void onMoreCommentClick(View view, DynamicDetailBeanV2 dynamicBean);
    }

    public interface OnCommentClickListener {
        void onCommentUserInfoClick(UserInfoBean userInfoBean);

        void onCommentContentClick(DynamicDetailBeanV2 dynamicBean, int position);

        void onCommentContentLongClick(DynamicDetailBeanV2 dynamicBean, int position);
    }

}
