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
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

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
    private static final int SHOW_MORE_COMMENT_SIZE_LIMIT = 4;

    private DynamicNoPullRecycleView mDynamicNoPullRecycleView;
    private TextView mMoreComment;


    private OnMoreCommentClickListener mOnMoreCommentClickListener;
    private OnCommentClickListener mOnCommentClickListener;
    private DynamicBean mDynamicBean;

    private boolean mIsUserNameClick = false; // 标识用户名被点击还是评论被点击了

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
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // JITTER_SPACING_TIME秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnMoreCommentClickListener != null) {
                            mOnMoreCommentClickListener.onMoreCommentClick(mMoreComment, mDynamicBean);
                        }
                    }
                });
        mDynamicNoPullRecycleView.setOnUserNameClickListener(new DynamicNoPullRecycleView.OnUserNameClickListener() {
            @Override
            public void onUserNameClick(UserInfoBean userInfoBean) {

                if (!mIsUserNameClick) {
                    if (mOnCommentClickListener != null) {
                        mOnCommentClickListener.onCommentUserInfoClick(userInfoBean);
                        mIsUserNameClick = true;
                    }
                }
            }
        });
        mDynamicNoPullRecycleView.setOnIitemClickListener(new SimpleTextNoPullRecycleView.OnIitemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (!mIsUserNameClick) {
                    if (mOnCommentClickListener != null) {
                        mOnCommentClickListener.onCommentContentClick(mDynamicBean, position);
                    }
                } else {
                    mIsUserNameClick = false;

                }
            }
        });
        mDynamicNoPullRecycleView.setOnUserNameLongClickListener(new DynamicNoPullRecycleView.OnUserNameLongClickListener() {
            @Override
            public void onUserNameLongClick(UserInfoBean userInfoBean) {
                if (!mIsUserNameClick) {
                    mIsUserNameClick = true;
                }
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("DynamicListCommentView is clicke");
            }
        });
    }

    /**
     * 设置数据
     *
     * @param dynamicBean
     */
    public void setData(DynamicBean dynamicBean) {
        mDynamicBean = dynamicBean;
        mDynamicNoPullRecycleView.setData(dynamicBean.getComments());
        if (dynamicBean.getTool().getFeed_comment_count() >= SHOW_MORE_COMMENT_SIZE_LIMIT) {
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


    public interface OnMoreCommentClickListener {
        void onMoreCommentClick(View view, DynamicBean dynamicBean);
    }

    public interface OnCommentClickListener {
        void onCommentUserInfoClick(UserInfoBean userInfoBean);

        void onCommentContentClick(DynamicBean dynamicBean, int position);
    }

}
