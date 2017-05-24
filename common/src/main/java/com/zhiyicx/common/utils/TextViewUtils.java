package com.zhiyicx.common.utils;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2015/5/12
 * @Contact master.jungle68@gmail.com
 */
public class TextViewUtils {

    private OnSpanTextClickListener mSpanTextClickListener;

    private TextView mTextView;//显示富文本的控件
    private String mOriMsg;//全部文本信息

    private boolean canNotRead = true;// 未付费状态

    private int mStartPos;

    private int mEndPos;

    private int mAlpha;

    private Integer mSpanTextColor;

    public static TextViewUtils newInstance(TextView textView, String oriMsg) {
        return new TextViewUtils(textView, oriMsg);
    }

    private TextViewUtils(TextView textView, String oriMsg) {
        mTextView = textView;
        mOriMsg = oriMsg;
        textView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
    }

    public TextViewUtils setSpanTextColor(int spanTextColor) {
        mSpanTextColor = spanTextColor;
        return this;
    }

    public TextViewUtils setOnSpanTextClickListener(OnSpanTextClickListener spanTextClickListener) {
        mSpanTextClickListener = spanTextClickListener;
        return this;
    }

    public TextViewUtils setPosition(int startPos, int endPos) {
        mStartPos = startPos;
        mEndPos = endPos;
        return this;
    }

    public TextViewUtils setAlpha(int alpha) {
        mAlpha = alpha;
        return this;
    }

    /**
     * 设置文字
     * @param canRead  是否可见
     * @return
     */
    public TextViewUtils disPlayText(boolean canRead) {
        if (mTextView == null)
            return null;
        if (canRead) {
            mTextView.setText(mOriMsg);
        } else {
            mTextView.setText(getSpannableString(mOriMsg));
        }
        return this;
    }


    class SpanTextClickable extends ClickableSpan implements View.OnClickListener {
        @Override
        public void onClick(View widget) {
            if (mSpanTextClickListener != null)
                mSpanTextClickListener.setSpanText(mTextView, canNotRead);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (mSpanTextColor == null) {
                ds.setColor(Color.BLACK);
            } else {
                ds.setColor(mSpanTextColor);
            }
            ds.setAlpha(mAlpha > 0 ? mAlpha : 0xff);
            ds.setUnderlineText(false);    //去除超链接的下划线
            if (canNotRead) {
                BlurMaskFilter blurMaskFilter = new BlurMaskFilter(mTextView.getTextSize() / 3, BlurMaskFilter.Blur.NORMAL);
                ds.setMaskFilter(blurMaskFilter);
            } else {
                ds.setMaskFilter(null);
            }
        }
    }

    private SpannableString getSpannableString(CharSequence temp) {
        SpannableString spanableInfo = new SpannableString(temp);
        spanableInfo.setSpan(new SpanTextClickable(), mStartPos, mEndPos, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    public interface OnSpanTextClickListener {
        void setSpanText(TextView view, boolean canNotRead);
    }
}
