package com.zhiyicx.thinksnsplus.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;


/**
 * @Describe 中心信息提示框
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public class TBCenterInfoPopWindow extends CustomPopupWindow {

    private CenterPopWindowItem1ClickListener mCenterPopWindowItem1ClickListener;

    private String titleStr;
    private String desStr;
    private String item1Str;

    private int titleColor;
    private int desColor;
    private int item1Color;
    private int imageResId;


    public static CBuilder builder() {
        return new CBuilder();
    }

    private TBCenterInfoPopWindow(CBuilder builder) {
        super(builder);
        this.titleStr = builder.titleStr;
        this.desStr = builder.desStr;
        this.item1Str = builder.item1Str;
        this.titleColor = builder.titleColor;
        this.desColor = builder.desColor;
        this.item1Color = builder.item1Color;
        this.imageResId = builder.imageResId;
        this.mCenterPopWindowItem1ClickListener = builder.mCenterPopWindowItem1ClickListener;
        initView();
    }

    protected void initView() {
        initTextView(titleStr, titleColor, R.id.ppw_center_title, null);
        initTextView(desStr, desColor, R.id.ppw_center_description, null);
        initTextView(item1Str, item1Color, R.id.ppw_center_item, mCenterPopWindowItem1ClickListener);
        if (imageResId != 0) {
            ImageView imageView = (ImageView) mContentView.findViewById(R.id.iv_top);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(imageResId);
        }
    }

    protected void initTextView(String text, int colorId, int resId, final CenterPopWindowItem1ClickListener clickListener) {
        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) mContentView.findViewById(resId);
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            if (colorId != 0) {
                textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClicked();
                    }
                }
            });
        }

    }

    public static final class CBuilder extends Builder {

        private CenterPopWindowItem1ClickListener mCenterPopWindowItem1ClickListener;
        private String titleStr;
        private String desStr;
        private String item1Str;

        private int titleColor;
        private int desColor;
        private int item1Color;
        private int imageResId;

        public CBuilder buildCenterPopWindowItem1ClickListener(CenterPopWindowItem1ClickListener mCenterPopWindowItem1ClickListener) {
            this.mCenterPopWindowItem1ClickListener = mCenterPopWindowItem1ClickListener;
            return this;
        }

        @Override
        public CBuilder backgroundAlpha(float alpha) {
            super.backgroundAlpha(alpha);
            return this;
        }

        @Override
        public CBuilder width(int width) {
            super.width(width);
            return this;
        }

        @Override
        public CBuilder height(int height) {
            super.height(height);
            return this;
        }

        public CBuilder desColor(int descrColor) {
            this.desColor = descrColor;
            return this;
        }

        public CBuilder item1Color(int item1Color) {
            this.item1Color = item1Color;
            return this;
        }

        public CBuilder imageResId(int resId) {
            this.imageResId = resId;
            return this;
        }

        public CBuilder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public CBuilder titleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public CBuilder desStr(String desStr) {
            this.desStr = desStr;
            return this;
        }

        public CBuilder item1Str(String item1Str) {
            this.item1Str = item1Str;
            return this;
        }


        @Override
        public CBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public CBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public CBuilder isFocus(boolean isFocus) {
            super.isFocus(isFocus);
            return this;
        }

        @Override
        public CBuilder backgroundDrawable(Drawable backgroundDrawable) {
            super.backgroundDrawable(backgroundDrawable);
            return this;
        }

        @Override
        public CBuilder animationStyle(int animationStyle) {
            super.animationStyle(animationStyle);
            return this;
        }

        @Override
        public CBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public TBCenterInfoPopWindow build() {
            contentViewId = R.layout.ppw_for_center_info_tb;
            isWrap = true;
            return new TBCenterInfoPopWindow(this);
        }
    }

    public interface CenterPopWindowItem1ClickListener {
        void onClicked();
    }

}
