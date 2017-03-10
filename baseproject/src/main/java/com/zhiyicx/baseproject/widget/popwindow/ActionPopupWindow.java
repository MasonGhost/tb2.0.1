package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @Describe 动作表单;
 * 使用场景：上传图片、删除操作确认、保存图片、性别选择
 * 动态/评论是否重发确认等情况
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */

public class ActionPopupWindow extends PopupWindow {
    public static final float POPUPWINDOW_ALPHA = .8f;

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private String mItem1Str;
    private String mItem2Str;
    private String mItem3Str;
    private String mBottomStr;
    private int mItem1Color;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private ActionPopupWindowItem1ClickListener mActionPopupWindowItem1ClickListener;
    private ActionPopupWindowItem2ClickListener mActionPopupWindowItem2ClickListener;
    private ActionPopupWindowItem3ClickListener mActionPopupWindowItem3ClickListener;
    private ActionPopupWindow.ActionPopupWindowBottomClickListener mActionPopupWindowBottomClickListener;

    public ActionPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.parentView;
        this.mItem1Str = builder.mItem1Str;
        this.mItem2Str = builder.mItem2Str;
        this.mItem3Str = builder.mItem3Str;
        this.mItem1Color=builder.mItem1Color;
        this.mBottomStr = builder.mBottomStr;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mActionPopupWindowItem1ClickListener = builder.mActionPopupWindowItem1ClickListener;
        this.mActionPopupWindowItem2ClickListener = builder.mActionPopupWindowItem2ClickListener;
        this.mActionPopupWindowItem3ClickListener = builder.mActionPopupWindowItem3ClickListener;
        this.mActionPopupWindowBottomClickListener = builder.mActionPopupWindowBottomClickListener;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    public void setItem1Str(String item1Str) {
        ((TextView) mContentView.findViewById(R.id.tv_pop_item1)).setText(item1Str);
    }

    public void setItem1StrColor(int res) {
        ((TextView) mContentView.findViewById(R.id.tv_pop_item1)).setTextColor(res);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.ppw_for_action, null);
        if (!TextUtils.isEmpty(mItem1Str)) {
            TextView item1View = (TextView) mContentView.findViewById(R.id.tv_pop_item1);
            item1View.setVisibility(View.VISIBLE);
            item1View.setText(mItem1Str);
            item1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionPopupWindowItem1ClickListener != null) {
                        mActionPopupWindowItem1ClickListener.onItem1Clicked();
                    }
                }
            });
            if(mItem1Color!=0){
                item1View.setTextColor(mItem1Color);
            }
        }
        if (!TextUtils.isEmpty(mItem2Str)) {
            TextView item2View = (TextView) mContentView.findViewById(R.id.tv_pop_item2);
            item2View.setVisibility(View.VISIBLE);
            item2View.setText(mItem2Str);
            item2View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionPopupWindowItem2ClickListener != null) {
                        mActionPopupWindowItem2ClickListener.onItem2Clicked();
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(mItem3Str)) {
            TextView item3View = (TextView) mContentView.findViewById(R.id.tv_pop_item3);
            item3View.setVisibility(View.VISIBLE);
            item3View.setText(mItem3Str);
            item3View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionPopupWindowItem3ClickListener != null) {
                        mActionPopupWindowItem3ClickListener.onItem3Clicked();
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

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });

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

    public static ActionPopupWindow.Builder builder() {
        return new ActionPopupWindow.Builder();
    }

    /**
     * 默认显示到底部
     */
    public void show() {
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
        dismiss();
    }

    public static final class Builder {
        private Activity mActivity;
        private View parentView;
        private String mItem1Str;
        private String mItem2Str;
        private String mItem3Str;
        private String mBottomStr;
        private int mItem1Color;
        private float mAlpha;
        private boolean mIsOutsideTouch = true;// 默认为true
        private boolean mIsFocus = true;// 默认为true
        private ActionPopupWindowItem1ClickListener mActionPopupWindowItem1ClickListener;
        private ActionPopupWindowItem2ClickListener mActionPopupWindowItem2ClickListener;
        private ActionPopupWindowItem3ClickListener mActionPopupWindowItem3ClickListener;
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

        public ActionPopupWindow.Builder item1Str(String item1Str) {
            this.mItem1Str = item1Str;
            return this;
        }

        public ActionPopupWindow.Builder item2Str(String item2Str) {
            this.mItem2Str = item2Str;
            return this;
        }

        public ActionPopupWindow.Builder item3Str(String item3Str) {
            this.mItem3Str = item3Str;
            return this;
        }

        public ActionPopupWindow.Builder bottomStr(String bottomStr) {
            this.mBottomStr = bottomStr;
            return this;
        }
        public ActionPopupWindow.Builder item1StrColor(int color) {
            this.mItem1Color = color;
            return this;
        }

        public ActionPopupWindow.Builder item1ClickListener(ActionPopupWindowItem1ClickListener actionPopupWindowItem1ClickListener) {
            this.mActionPopupWindowItem1ClickListener = actionPopupWindowItem1ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item2ClickListener(ActionPopupWindowItem2ClickListener actionPopupWindowItem2ClickListener) {
            this.mActionPopupWindowItem2ClickListener = actionPopupWindowItem2ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item3ClickListener(ActionPopupWindowItem3ClickListener actionPopupWindowItem3ClickListener) {
            this.mActionPopupWindowItem3ClickListener = actionPopupWindowItem3ClickListener;
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

        public ActionPopupWindow.Builder backgroundAlpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public ActionPopupWindow build() {
            return new ActionPopupWindow(this);
        }
    }

    public interface ActionPopupWindowItem1ClickListener {
        void onItem1Clicked();
    }

    public interface ActionPopupWindowItem2ClickListener {
        void onItem2Clicked();
    }

    public interface ActionPopupWindowItem3ClickListener {
        void onItem3Clicked();
    }

    public interface ActionPopupWindowBottomClickListener {
        void onBottomClicked();
    }

}