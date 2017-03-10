package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhiyicx.baseproject.widget.InputLimitView;

/**
 * @Describe 输入表单;
 * 使用场景：动态评论
 * @Author Jungle68
 * @Date 2017/3/4
 * @Contact master.jungle68@gmail.com
 */

public class InputPopupWindow extends PopupWindow {
    public static final float POPUPWINDOW_ALPHA = .8f;

    private Activity mActivity;
    private View mParentView;
    private InputLimitView mContentView;

    private int mLimitMaxSize;// 最大输入值
    private int mShowLimitSize;// 当输入值达到 mshowLimitSize 时，显示提示
    private String mContentHint = "";
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private InputLimitView.OnSendClickListener mOnSendClickListener;

    public InputPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.parentView;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mContentHint = builder.mContentHint;
        this.mOnSendClickListener = builder.mOnSendClickListener;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
//        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = new InputLimitView(mActivity, mLimitMaxSize, mShowLimitSize);
        mContentView.setEtContentHint(mContentHint);
        mContentView.setOnSendClickListener(new InputLimitView.OnSendClickListener() {
            @Override
            public void onSendClick(View v,String text) {
                if (mOnSendClickListener != null) {
                    mOnSendClickListener.onSendClick(v,text);
                }
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });

    }

    public void setTxtHint(String hintStr) {
        mContentView.setEtContentHint(hintStr);
    }

    /**
     * 设置屏幕的透明度
     *
     * @param alpha 需要设置透明度
     */
    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public static InputPopupWindow.Builder builder() {
        return new InputPopupWindow.Builder();
    }

    /**
     * 默认显示到底部
     */
    public void show() {
        mContentView.getFocus();
        setWindowAlpha(mAlpha);
        if (mParentView == null) {
            showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        } else {
            showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 隐藏popupwindow
     */
    public void hide() {
        mContentView.clearFocus();
        dismiss();
    }

    public static final class Builder {
        private Activity mActivity;
        private View parentView;

        private int mLimitMaxSize;// 最大输入值
        private int mShowLimitSize;// 当输入值达到 mshowLimitSize 时，显示提示
        private String mContentHint;
        private float mAlpha;
        private boolean mIsOutsideTouch = true;// 默认为true
        private boolean mIsFocus = true;// 默认为true
        private InputLimitView.OnSendClickListener mOnSendClickListener;

        private Builder() {
        }

        public InputPopupWindow.Builder with(Activity activity) {
            this.mActivity = activity;
            return this;
        }

        public InputPopupWindow.Builder parentView(View parentView) {
            this.parentView = parentView;
            return this;
        }

        public InputPopupWindow.Builder limitMaxSize(int limitMaxSize) {
            this.mLimitMaxSize = limitMaxSize;
            return this;
        }

        public InputPopupWindow.Builder showLimitSize(int showLimitSize) {
            this.mShowLimitSize = showLimitSize;
            return this;
        }

        public InputPopupWindow.Builder contentHint(String contentHint) {
            this.mContentHint = contentHint;
            return this;
        }


        public InputPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public InputPopupWindow.Builder isFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public InputPopupWindow.Builder onSendClickListener(InputLimitView.OnSendClickListener l) {
            this.mOnSendClickListener = l;
            return this;
        }

        public InputPopupWindow.Builder backgroundAlpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public InputPopupWindow build() {
            return new InputPopupWindow(this);
        }
    }


}