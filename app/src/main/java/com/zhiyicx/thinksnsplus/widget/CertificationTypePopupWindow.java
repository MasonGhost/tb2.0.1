package com.zhiyicx.thinksnsplus.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 选择个人或者企业认证
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public class CertificationTypePopupWindow extends PopupWindow{

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private float mAlpha;
    private String mCancel;
    private int mWidth;
    private int mHeight;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private OnTypeSelectListener mListener;

    public CertificationTypePopupWindow() {
    }

    public CertificationTypePopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mAlpha = builder.mAlpha;
        this.mCancel = builder.mCancel;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mListener = builder.mListener;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
        setBackgroundDrawable(mBackgroundDrawable);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_certification_type, null);
        TextView tvCertificationPersonage = (TextView) mContentView.findViewById(R.id.tv_certification_personage);
        TextView tvCertificationCompany = (TextView) mContentView.findViewById(R.id.tv_certification_company);
        TextView tvCancel = (TextView) mContentView.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dismiss());
        tvCertificationPersonage.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onTypeSelected(0);
            }
        });
        tvCertificationCompany.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onTypeSelected(1);
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
        showAtLocation(mParentView == null ? mContentView : mParentView, Gravity.BOTTOM, 0, 0);
    }

    public void hide() {
        dismiss();
    }

    public static CertificationTypePopupWindow.Builder Builder() {
        return new CertificationTypePopupWindow.Builder();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private float mAlpha;
        private int mWidth = 0;
        private int mHeight = 0;
        private String mCancel;
        private OnTypeSelectListener mListener;

        private Builder() {
        }

        public CertificationTypePopupWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public CertificationTypePopupWindow.Builder width(int width) {
            this.mWidth = width;
            return this;
        }

        public CertificationTypePopupWindow.Builder height(int height) {
            this.mHeight = height;
            return this;
        }

        public CertificationTypePopupWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public CertificationTypePopupWindow.Builder cancel(String cancel) {
            this.mCancel = cancel;
            return this;
        }

        public CertificationTypePopupWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public CertificationTypePopupWindow.Builder setListener(OnTypeSelectListener listener){
            this.mListener = listener;
            return this;
        }

        public CertificationTypePopupWindow build() {
            return new CertificationTypePopupWindow(this);
        }
    }

    public interface OnTypeSelectListener{
        void onTypeSelected(int position);
    }
}
