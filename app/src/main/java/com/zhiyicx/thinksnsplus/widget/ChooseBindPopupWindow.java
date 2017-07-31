package com.zhiyicx.thinksnsplus.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 第三方登陆选择绑定或者新注册
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class ChooseBindPopupWindow extends PopupWindow{

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private float mAlpha;
    private String mCancel;
    private OnItemChooseListener mItemClickListener;
    private int mWidth;
    private int mHeight;
    private int mItemLayout;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;

    public ChooseBindPopupWindow() {
    }

    public ChooseBindPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mAlpha = builder.mAlpha;
        this.mCancel = builder.mCancel;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mItemLayout = builder.mItemLayout;
        this.mItemClickListener = builder.mItemChooseListener;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(false);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
        setBackgroundDrawable(mBackgroundDrawable);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_choose_bind, null);
        TextView chooseToCompleteAccount = (TextView) mContentView.findViewById(R.id.choose_to_complete_account);
        TextView chooseToBindAccount = (TextView) mContentView.findViewById(R.id.choose_to_bind_account);
        chooseToCompleteAccount.setOnClickListener(v -> {
            if (mItemClickListener != null){
                mItemClickListener.onItemChose(0);
            }
        });
        chooseToBindAccount.setOnClickListener(v -> {
            if (mItemClickListener != null){
                mItemClickListener.onItemChose(1);
            }
        });
        setOnDismissListener(() -> setWindowAlpha(1.0f));
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        setWindowAlpha(mAlpha);
        showAtLocation(mParentView == null ? mContentView : mParentView, Gravity.CENTER, 0, 0);
    }

    public void hide() {
        dismiss();
    }

    public static ChooseBindPopupWindow.Builder Builder() {
        return new ChooseBindPopupWindow.Builder();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private float mAlpha;
        private int mWidth = 0;
        private int mHeight = 0;
        private int mItemLayout = 0;
        private String mCancel;
        private OnItemChooseListener mItemChooseListener;

        private Builder() {
        }

        public ChooseBindPopupWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public ChooseBindPopupWindow.Builder itemLayout(int itemLayout) {
            this.mItemLayout = itemLayout;
            return this;
        }

        public ChooseBindPopupWindow.Builder width(int width) {
            this.mWidth = width;
            return this;
        }

        public ChooseBindPopupWindow.Builder height(int height) {
            this.mHeight = height;
            return this;
        }

        public ChooseBindPopupWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public ChooseBindPopupWindow.Builder cancel(String cancel) {
            this.mCancel = cancel;
            return this;
        }

        public ChooseBindPopupWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public ChooseBindPopupWindow.Builder itemListener(OnItemChooseListener itemChooseListener) {
            this.mItemChooseListener = itemChooseListener;
            return this;
        }

        public ChooseBindPopupWindow build() {
            return new ChooseBindPopupWindow(this);
        }
    }

    public interface OnItemChooseListener{
        void onItemChose(int position);
    }

}
