package com.zhiyicx.thinksnsplus.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UpdateInfoBean;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe 检查更新的提示弹窗
 * @date 2017/7/13
 * @contact email:648129313@qq.com
 */

public class CheckVersionPopupWindow extends PopupWindow {

    private TextView mTvVersionName;
    private TextView mTvFileLength;
    private TextView mTvUpdateContent;
    private TextView mTvUpdateNow;
    private TextView mTvIgnore;
    private ProgressBar mPbDownload;

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha;
    private int mWidth;
    private int mHeight;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private UpdateInfoBean mUpdateInfoBean;

    private OnUpdateClickListener mListener;

    public CheckVersionPopupWindow(Context context) {
        super(context);
    }

    public CheckVersionPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mUpdateInfoBean = builder.mData;
        this.mListener = builder.mListener;
        initView();
    }

    private void initView() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_check_version, null);
        mTvVersionName = (TextView) mContentView.findViewById(R.id.tv_version_name);
        mTvFileLength = (TextView) mContentView.findViewById(R.id.tv_file_length);
        mTvUpdateContent = (TextView) mContentView.findViewById(R.id.tv_update_content);
        mTvUpdateNow = (TextView) mContentView.findViewById(R.id.tv_update_now);
        mTvIgnore = (TextView) mContentView.findViewById(R.id.tv_ignore);
        mPbDownload = (ProgressBar) mContentView.findViewById(R.id.pb_download);
        initData();
        setWidth((int) (DeviceUtils.getScreenWidth(mActivity) * 0.8));
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setBackgroundDrawable(mBackgroundDrawable);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
        setOnDismissListener(() -> setWindowAlpha(1.0f));
    }

    private void initData() {
        if (mUpdateInfoBean != null && mUpdateInfoBean.isHasNewVersion()) {
            mTvVersionName.setText(String.format(mActivity.getString(R.string.version_name), mUpdateInfoBean.getName()));
            mTvFileLength.setText(String.format(mActivity.getString(R.string.version_content_length),
                    String.valueOf(mUpdateInfoBean.getLength())));
            mTvUpdateContent.setText(mUpdateInfoBean.getContent());
            // 忽略这个版本
            RxView.clicks(mTvIgnore)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> dismiss());
            // 立即更新
            RxView.clicks(mTvUpdateNow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        if (mListener != null) {
                            mListener.onUpdateClick();
                        }
                    });
        }
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

    /**
     * 更新进度条
     *
     * @param progress 进度
     */
    public void updateProgress(int progress) {
        boolean isFinish = progress < 100;
        mPbDownload.setVisibility(isFinish ? View.VISIBLE : View.GONE);
        mTvUpdateNow.setVisibility(isFinish ? View.GONE : View.VISIBLE);
        mTvIgnore.setVisibility(isFinish ? View.GONE : View.VISIBLE);
        mPbDownload.setProgress(progress);
    }

    public static CheckVersionPopupWindow.Builder Builder() {
        return new CheckVersionPopupWindow.Builder();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private boolean mIsOutsideTouch = true;
        private boolean mIsFocus = true;
        private float mAlpha;
        private int mWidth = 0;
        private int mHeight = 0;
        private UpdateInfoBean mData;
        private OnUpdateClickListener mListener;

        public Builder() {
        }

        public CheckVersionPopupWindow.Builder with(Activity activity) {
            this.mActivity = activity;
            return this;
        }

        public CheckVersionPopupWindow.Builder width(int width) {
            this.mWidth = width;
            return this;
        }

        public CheckVersionPopupWindow.Builder height(int height) {
            this.mHeight = height;
            return this;
        }

        public CheckVersionPopupWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public CheckVersionPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public CheckVersionPopupWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public CheckVersionPopupWindow.Builder bindData(UpdateInfoBean data) {
            this.mData = data;
            return this;
        }

        public CheckVersionPopupWindow.Builder bindListener(OnUpdateClickListener listener) {
            this.mListener = listener;
            return this;
        }

        public CheckVersionPopupWindow build() {
            return new CheckVersionPopupWindow(this);
        }
    }

    public interface OnUpdateClickListener {
        void onUpdateClick();
    }

}
