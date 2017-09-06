package com.zhiyicx.thinksnsplus.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 答案选择设置列表排序的弹框
 * @date 2017/8/17
 * @contact email:648129313@qq.com
 */

public class QuestionSelectListTypePopWindow extends PopupWindow {

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private float mAlpha;
    private OnOrderTypeSelectListener mListener;

    public QuestionSelectListTypePopWindow() {
    }

    public QuestionSelectListTypePopWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mAlpha = builder.mAlpha;
        this.mListener = builder.mListener;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(mContentView.getResources().getDimensionPixelOffset(R.dimen.question_list_type_width));
        setHeight(mContentView.getResources().getDimensionPixelOffset(R.dimen.question_list_type_height));
        setFocusable(false);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));

        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_select_list_type, null);
        AppCompatCheckedTextView mTvDefault = (AppCompatCheckedTextView) mContentView.findViewById(R.id.tv_default);
        AppCompatCheckedTextView mTvTime = (AppCompatCheckedTextView) mContentView.findViewById(R.id.tv_time);
        mTvDefault.setOnClickListener(v -> {
            mTvDefault.toggle();
            mTvTime.toggle();
            if (mTvDefault.isChecked()) {
                if (mListener != null) {
                    mListener.onOrderTypeSelected(0);
                }
            }
        });
        mTvTime.setOnClickListener(v -> {
            mTvTime.toggle();
            mTvDefault.toggle();
            if (mTvTime.isChecked()) {
                if (mListener != null) {
                    mListener.onOrderTypeSelected(1);
                }
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
        if (isShowing()) {
            dismiss();
            return;
        }
        setWindowAlpha(mAlpha);
        int width = UIUtils.getWindowWidth(mActivity) - mContentView.getResources().getDimensionPixelOffset(R.dimen.qa_top_select_width);
        showAsDropDown(mParentView == null ? mContentView : mParentView, width, 10);
    }

    public void hide() {
        dismiss();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private float mAlpha;
        private OnOrderTypeSelectListener mListener;

        private Builder() {
        }

        public QuestionSelectListTypePopWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public QuestionSelectListTypePopWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public QuestionSelectListTypePopWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public QuestionSelectListTypePopWindow.Builder setListener(OnOrderTypeSelectListener listener) {
            this.mListener = listener;
            return this;
        }

        public QuestionSelectListTypePopWindow build() {
            return new QuestionSelectListTypePopWindow(this);
        }
    }

    public static QuestionSelectListTypePopWindow.Builder Builder() {
        return new QuestionSelectListTypePopWindow.Builder();
    }

    public interface OnOrderTypeSelectListener {
        void onOrderTypeSelected(int type);
    }
}
