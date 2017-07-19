package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表 评论控件
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class GroupDynamicListCommentView extends LinearLayout {
    private static final int SHOW_MORE_COMMENT_SIZE_LIMIT = 6;

    private GroupDynamicNoPullRecycleView mGroupDynamicNoPullRecycleView;
    private TextView mMoreComment;


    private OnMoreCommentClickListener mOnMoreCommentClickListener;
    private OnCommentClickListener mOnCommentClickListener;
    private GroupDynamicNoPullRecycleView.OnCommentStateClickListener mOnCommentStateClickListener;
    private GroupDynamicListBean mDynamicBean;

    private boolean mIsUserNameClick = false; // 标识用户名被点击还是评论被点击了

    public GroupDynamicListCommentView(Context context) {
        super(context);
        init();
    }

    public GroupDynamicListCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupDynamicListCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.view_group_dynamic_list_comment, this);
        mGroupDynamicNoPullRecycleView = (GroupDynamicNoPullRecycleView) findViewById(R.id.fl_comment);
        mMoreComment = (TextView) findViewById(R.id.tv_more_comment);
        setListener();
    }

    private void setListener() {
        RxView.clicks(mMoreComment)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // JITTER_SPACING_TIME秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mOnMoreCommentClickListener != null) {
                        mOnMoreCommentClickListener.onMoreCommentClick(mMoreComment, mDynamicBean);
                    }
                });
        mGroupDynamicNoPullRecycleView.setOnUserNameClickListener(userInfoBean -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentUserInfoClick(userInfoBean);
                    mIsUserNameClick = true;
                }
            }
        });
        mGroupDynamicNoPullRecycleView.setOnIitemClickListener((view, position) -> {

            if (!mIsUserNameClick) {
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onCommentContentClick(mDynamicBean, position);
                }
            } else {
                mIsUserNameClick = false;

            }
        });
        mGroupDynamicNoPullRecycleView.setOnUserNameLongClickListener(userInfoBean -> {
            if (!mIsUserNameClick) {
                mIsUserNameClick = true;
            }
        });
        setOnClickListener(v -> {
        });
        mGroupDynamicNoPullRecycleView.setOnCommentStateClickListener((dynamicCommentBean, position) -> {
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
    public void setData(GroupDynamicListBean dynamicBean) {
        mDynamicBean = dynamicBean;
        List<GroupDynamicCommentListBean> data = new ArrayList<>();

        if (dynamicBean.getNew_comments() != null && !dynamicBean.getNew_comments().isEmpty()) {
            if (dynamicBean.getNew_comments().size() >= SHOW_MORE_COMMENT_SIZE_LIMIT) { //最多显示3条
                for (int i = 0; i < SHOW_MORE_COMMENT_SIZE_LIMIT - 1; i++) {
                    data.add(dynamicBean.getNew_comments().get(i));
                }
            } else {
                data.addAll(dynamicBean.getNew_comments());
            }
        }
        mGroupDynamicNoPullRecycleView.setData(data);
        if (dynamicBean.getComments() >= SHOW_MORE_COMMENT_SIZE_LIMIT) {
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

    public void setOnCommentStateClickListener(GroupDynamicNoPullRecycleView.OnCommentStateClickListener onCommentStateClickListener) {
        mOnCommentStateClickListener = onCommentStateClickListener;
    }

    public interface OnMoreCommentClickListener {
        void onMoreCommentClick(View view, GroupDynamicListBean dynamicBean);
    }

    public interface OnCommentClickListener {
        void onCommentUserInfoClick(UserInfoBean userInfoBean);

        void onCommentContentClick(GroupDynamicListBean dynamicBean, int position);
    }

}
