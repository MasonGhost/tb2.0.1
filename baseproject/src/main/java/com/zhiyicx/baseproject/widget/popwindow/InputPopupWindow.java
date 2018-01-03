package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;

/**
 * @Author Jliuer
 * @Date 2018/01/03/9:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InputPopupWindow extends CustomPopupWindow {

    private InputPopupWindow.ClickListener mClickListener;

    private String titleStr;
    private String subTitleStr;
    private String input1Str;
    private String input2Str;
    private String confirmStr;
    private String cancleStr;

    private int titleColor;
    private int subTitleColor;
    private int input1Color;
    private int input2Color;
    private int confirmColor;
    private int cancleColor;


    public InputPopupWindow(CBuilder builder) {
        super(builder);

        this.titleStr = builder.titleStr;
        this.subTitleStr = builder.subTitleStr;
        this.input1Str = builder.input1Str;
        this.input2Str = builder.input2Str;
        this.confirmStr = builder.confirmStr;
        this.cancleStr = builder.cancleStr;

        this.titleColor = builder.titleColor;
        this.subTitleColor = builder.subTitleColor;
        this.input1Color = builder.input1Color;
        this.input2Color = builder.input2Color;
        this.confirmColor = builder.confirmColor;
        this.cancleStr = builder.cancleStr;

        this.mClickListener = builder.mClickListener;

        initView();
    }

    public static CBuilder builder() {
        return new CBuilder();
    }

    public CBuilder newBuilder() {
        return new CBuilder(this);
    }

    private void initView() {
        TextView title = (TextView) mContentView.findViewById(R.id.tv_tittle);
    }

    public static final class CBuilder extends Builder {

        private InputPopupWindow.ClickListener mClickListener;

        private String titleStr;
        private String subTitleStr;
        private String input1Str;
        private String input2Str;
        private String confirmStr;
        private String cancleStr;

        private int titleColor;
        private int subTitleColor;
        private int input1Color;
        private int input2Color;
        private int confirmColor;
        private int cancleColor;

        public CBuilder() {
        }

        public CBuilder(InputPopupWindow builder) {
            this.titleStr = builder.titleStr;
            this.subTitleStr = builder.subTitleStr;
            this.input1Str = builder.input1Str;
            this.input2Str = builder.input2Str;
            this.confirmStr = builder.confirmStr;
            this.cancleStr = builder.cancleStr;

            this.titleColor = builder.titleColor;
            this.subTitleColor = builder.subTitleColor;
            this.input1Color = builder.input1Color;
            this.input2Color = builder.input2Color;
            this.confirmColor = builder.confirmColor;
            this.cancleStr = builder.cancleStr;

            this.mClickListener = builder.mClickListener;

        }

        public InputPopupWindow.CBuilder buildClickListener(InputPopupWindow.ClickListener mClickListener) {
            this.mClickListener = mClickListener;
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder backgroundAlpha(float alpha) {
            super.backgroundAlpha(alpha);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder width(int width) {
            super.width(width);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder height(int height) {
            super.height(height);
            return this;
        }

        public InputPopupWindow.CBuilder buildConfirmColorColor(int confirmColor) {
            this.confirmColor = confirmColor;
            return this;
        }

        public InputPopupWindow.CBuilder buildInput2Color(int input2Color) {
            this.input2Color = input2Color;
            return this;
        }

        public InputPopupWindow.CBuilder buildInput1Color(int input1Color) {
            this.input1Color = input1Color;
            return this;
        }

        public InputPopupWindow.CBuilder buildSubTitleColor(int subTitleColor) {
            this.subTitleColor = subTitleColor;
            return this;
        }

        public InputPopupWindow.CBuilder buildTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public InputPopupWindow.CBuilder buildInput2Str(String input2Str) {
            this.input2Str = input2Str;
            return this;
        }

        public InputPopupWindow.CBuilder buildTitleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public InputPopupWindow.CBuilder buildSubTitleStr(String subTitleStr) {
            this.subTitleStr = subTitleStr;
            return this;
        }

        public InputPopupWindow.CBuilder buildInput1Str(String input1Str) {
            this.input1Str = input1Str;
            return this;
        }

        public InputPopupWindow.CBuilder buildConfirmStr(String confirmStr) {
            this.confirmStr = confirmStr;
            return this;
        }

        public InputPopupWindow.CBuilder buildCancleStr(String cancleStr) {
            this.cancleStr = cancleStr;
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder contentView(int contentViewId) {
            super.contentView(contentViewId);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder isWrap(boolean isWrap) {
            super.isWrap(isWrap);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder customListener(CustomPopupWindowListener listener) {
            super.customListener(listener);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder isFocus(boolean isFocus) {
            super.isFocus(isFocus);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder backgroundDrawable(Drawable backgroundDrawable) {
            super.backgroundDrawable(backgroundDrawable);
            return this;
        }

        @Override
        public InputPopupWindow.CBuilder animationStyle(int animationStyle) {
            super.animationStyle(animationStyle);
            return this;
        }

        @Override
        public InputPopupWindow build() {
            contentViewId = R.layout.pop_for_input;
            isWrap = true;
            return new InputPopupWindow(this);
        }
    }

    public interface ClickListener {
        void onConfirm(String item1, String item2);

        void onCancle();
    }
}
