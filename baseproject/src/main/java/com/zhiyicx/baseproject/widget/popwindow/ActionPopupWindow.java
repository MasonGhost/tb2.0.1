package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @Describe 动作表单;
 * 使用场景：上传图片、删除操作确认、保存图片、性别选择、
 * 动态/评论是否重发确认等情况
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */

public class ActionPopupWindow extends PopupWindow {
    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private String mTopStr;
    private String mCenterStr;
    private String mBottomStr;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private ActionPopupWindow.ActionPopupWindowTopClickListener mActionPopupWindowTopClickListener;
    private ActionPopupWindow.ActionPopupWindowCenterClickListener mActionPopupWindowCenterClickListener;
    private ActionPopupWindow.ActionPopupWindowBottomClickListener mActionPopupWindowBottomClickListener;

    public ActionPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.parentView;
        this.mTopStr = builder.mTopStr;
        this.mCenterStr = builder.mCenterStr;
        this.mBottomStr = builder.mBottomStr;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mActionPopupWindowTopClickListener = builder.mActionPopupWindowTopClickListener;
        this.mActionPopupWindowCenterClickListener = builder.mActionPopupWindowCenterClickListener;
        this.mActionPopupWindowBottomClickListener = builder.mActionPopupWindowBottomClickListener;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        setAnimationStyle(R.style.actionPopupAnimation);
        setContentView(mContentView);


    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_for_action, null);
        if (!TextUtils.isEmpty(mTopStr)) {
            TextView topView = (TextView) mContentView.findViewById(R.id.tv_pop_top);
            topView.setVisibility(View.VISIBLE);
            topView.setText(mTopStr);
            topView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionPopupWindowTopClickListener != null) {
                        mActionPopupWindowTopClickListener.onTopClicked();
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(mCenterStr)) {
            TextView centerView = (TextView) mContentView.findViewById(R.id.tv_pop_center);
            centerView.setVisibility(View.VISIBLE);
            centerView.setText(mCenterStr);
            centerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionPopupWindowCenterClickListener != null) {
                        mActionPopupWindowCenterClickListener.onCenterClicked();
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(mBottomStr)) {
            TextView bottomView = (TextView) mContentView.findViewById(R.id.tv_pop_bottom);
            bottomView.setVisibility(View.VISIBLE);
            bottomView.setText(mBottomStr);
            bottomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionPopupWindowBottomClickListener != null) {
                        mActionPopupWindowBottomClickListener.onBottomClicked();
                    }
                }
            });
        }
    }


    public static ActionPopupWindow.Builder builder() {
        return new ActionPopupWindow.Builder();
    }

    /**
     * 默认显示到中间
     */
    public void show() {
        if (mParentView == null) {
            showAsDropDown(mContentView, 0, 0);
        } else {
            showAsDropDown(mParentView, 0, 0);
        }
    }

    public static final class Builder {
        private Activity mActivity;
        private View parentView;
        private String mTopStr;
        private String mCenterStr;
        private String mBottomStr;

        private boolean mIsOutsideTouch = true;// 默认为true
        private boolean mIsFocus = true;// 默认为true
        private ActionPopupWindow.ActionPopupWindowTopClickListener mActionPopupWindowTopClickListener;
        private ActionPopupWindow.ActionPopupWindowCenterClickListener mActionPopupWindowCenterClickListener;
        private ActionPopupWindow.ActionPopupWindowBottomClickListener mActionPopupWindowBottomClickListener;

        private Builder() {
        }

        public ActionPopupWindow.Builder with(Activity activity) {
            this.mActivity = activity;
            return this;
        }

        public ActionPopupWindow.Builder parentView(View parentView) {
            this.parentView = parentView;
            return this;
        }

        public ActionPopupWindow.Builder topStr(String topStr) {
            this.mTopStr = topStr;
            return this;
        }

        public ActionPopupWindow.Builder centerStr(String centerStr) {
            this.mCenterStr = centerStr;
            return this;
        }

        public ActionPopupWindow.Builder bottomStr(String bottomStr) {
            this.mBottomStr = bottomStr;
            return this;
        }

        public ActionPopupWindow.Builder popClickListener(ActionPopupWindow.ActionPopupWindowTopClickListener actionPopupWindowTopClickListener) {
            this.mActionPopupWindowTopClickListener = actionPopupWindowTopClickListener;
            return this;
        }

        public ActionPopupWindow.Builder centerClickListener(ActionPopupWindow.ActionPopupWindowCenterClickListener actionPopupWindowCenterClickListener) {
            this.mActionPopupWindowCenterClickListener = actionPopupWindowCenterClickListener;
            return this;
        }

        public ActionPopupWindow.Builder bottomClickListener(ActionPopupWindow.ActionPopupWindowBottomClickListener actionPopupWindowBottomClickListener) {
            this.mActionPopupWindowBottomClickListener = actionPopupWindowBottomClickListener;
            return this;
        }


        public ActionPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public ActionPopupWindow.Builder isFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public ActionPopupWindow build() {
            return new ActionPopupWindow(this);
        }
    }

    public interface ActionPopupWindowTopClickListener {
        void onTopClicked();
    }

    public interface ActionPopupWindowCenterClickListener {
        void onCenterClicked();
    }

    public interface ActionPopupWindowBottomClickListener {
        void onBottomClicked();
    }
}