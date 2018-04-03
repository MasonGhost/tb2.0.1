package com.zhiyicx.thinksnsplus.widget;

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

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 第三方登陆选择绑定或者新注册
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class ChooseBindPopupWindow extends PopupWindow {

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private float mAlpha;
    private String mCancel;
    private String mItem1Str;
    private String mItem2Str;
    private String mItem3Str;
    protected boolean isOutsideTouch;
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
        this.isOutsideTouch = builder.isOutsideTouch;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mItem1Str = builder.mItem1Str;
        this.mItem2Str = builder.mItem2Str;
        this.mItem3Str = builder.mItem3Str;
        this.mItemLayout = builder.mItemLayout;
        this.mItemClickListener = builder.mItemChooseListener;
        initView();
    }

    private void initView() {
        initLayout();
        if (mWidth * mHeight == 0) {
            setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            setWidth(mWidth);
            setHeight(mHeight);
        }

        setFocusable(false);
        setOutsideTouchable(isOutsideTouch);
//        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
        setBackgroundDrawable(mBackgroundDrawable);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(mItemLayout != 0 ? mItemLayout : R.layout.pop_choose_bind, null);
        TextView item1 = (TextView) mContentView.findViewById(R.id.item_1);
        TextView item2 = (TextView) mContentView.findViewById(R.id.item_2);
        TextView item3 = (TextView) mContentView.findViewById(R.id.item_3);

        int itemHeight = mContentView.getContext().getResources().getDimensionPixelOffset(mItemLayout != 0 ? R.dimen
                .pop_choose_third_type_height_samll : R.dimen.pop_choose_third_type_height);
        int space = mContentView.getContext().getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        item1.setVisibility(!TextUtils.isEmpty(mItem1Str) ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(mItem1Str)) {
            mHeight += itemHeight;
        }
        item1.setText(mItem1Str);

        item2.setVisibility(!TextUtils.isEmpty(mItem2Str) ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(mItem2Str)) {
            mHeight += itemHeight;
        }
        item2.setText(mItem2Str);

        item3.setVisibility(!TextUtils.isEmpty(mItem3Str) ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(mItem3Str)) {
            mHeight += itemHeight;
        }
        item3.setText(mItem3Str);

        mHeight += space;
        mWidth = LinearLayout.LayoutParams.WRAP_CONTENT;

        item1.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemChose(0);
            }
        });
        item2.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemChose(1);
            }
        });
        item3.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemChose(2);
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

    /**
     * 显示特定位置
     * @param view
     */
    public void showdefine(View view) {
        int windowPos[] = CustomPopupWindow.calculatePopWindowPos(view, mContentView);
        int xOff = view.getWidth();// 可以自己调整偏移
        windowPos[0] -= xOff;
        showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    public void showLeft() {
        setWindowAlpha(mAlpha);
        showAtLocation(mParentView == null ? mContentView : mParentView, Gravity.LEFT, 0, 0);
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
        protected boolean isOutsideTouch = false;
        private int mHeight = 0;
        private int mItemLayout = 0;
        private String mCancel;
        private String mItem1Str;
        private String mItem2Str;
        private String mItem3Str;
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

        public ChooseBindPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.isOutsideTouch = isOutsideTouch;
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

        public ChooseBindPopupWindow.Builder itemlStr(String itemlStr) {
            this.mItem1Str = itemlStr;
            return this;
        }

        public ChooseBindPopupWindow.Builder item2Str(String item2Str) {
            this.mItem2Str = item2Str;
            return this;
        }

        public ChooseBindPopupWindow.Builder item3Str(String item3Str) {
            this.mItem3Str = item3Str;
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

    public interface OnItemChooseListener {
        void onItemChose(int position);
    }

    public void canNotRegiterByThirdPlatform(boolean openThirdRegister) {
        mContentView.findViewById(R.id.item_2).setVisibility(openThirdRegister ? View.VISIBLE : View.GONE);
    }


}
