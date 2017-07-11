package com.zhiyicx.common.utils;

import android.graphics.BlurMaskFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * @Describe for TextView click spannable  and Blur effect
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

    private int mDynamicPosition;// 动态位置

    private int mNote;// 付费节点

    private int mAmount;// 付费金额

    private Integer mSpanTextColor;
    private boolean mCanRead;

    private int mMaxLineNums = 3;

    public static TextViewUtils newInstance(TextView textView, String oriMsg) {
        return new TextViewUtils(textView, oriMsg);
    }

    private TextViewUtils(TextView textView, String oriMsg) {
        this.mTextView = textView;
        this.mOriMsg = oriMsg;
    }

    public TextViewUtils spanTextColor(int spanTextColor) {
        mSpanTextColor = spanTextColor;
        return this;
    }

    public TextViewUtils onSpanTextClickListener(OnSpanTextClickListener spanTextClickListener) {
        mSpanTextClickListener = spanTextClickListener;
        return this;
    }

    public TextViewUtils position(int startPos, int endPos) {
        mStartPos = startPos;
        mEndPos = endPos;
        return this;
    }

    public TextViewUtils alpha(int alpha) {
        mAlpha = alpha;
        return this;
    }

    public TextViewUtils amount(int amount) {
        mAmount = amount;
        return this;
    }

    public TextViewUtils note(int note) {
        mNote = note;
        return this;
    }

    public TextViewUtils dynamicPosition(int dynamicPosition) {
        mDynamicPosition = dynamicPosition;
        return this;
    }

    public TextViewUtils maxLines(int maxlines) {
        mMaxLineNums = maxlines;
        return this;
    }

    /**
     * 设置文字
     *
     * @param canRead 是否可见
     * @return
     */
    public TextViewUtils disPlayText(boolean canRead) {
        mCanRead = canRead;
        return this;
    }

    public TextViewUtils build() {
        if (mTextView == null) {
            throw new IllegalArgumentException("textView not be null");
        }
        try {
            handleTextDisplay();
        } catch (Exception e) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(mOriMsg);
        }
        return this;
    }


    private void handleTextDisplay() throws Exception {
        mTextView.setVisibility(View.INVISIBLE);

        if (mTextView.getLineCount() > mMaxLineNums) {
            int endOfLastLine = mTextView.getLayout().getLineEnd(mMaxLineNums - 1);
            mOriMsg = mOriMsg.subSequence(0, endOfLastLine - 2) + "...";
        }
        if (!mCanRead) {
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
            ViewTreeObserver viewTreeObserver = mTextView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver viewTreeObserver = mTextView.getViewTreeObserver();
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                    mTextView.setText(getSpannableString(mOriMsg));
                    mTextView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(mOriMsg);
        }
    }

    class SpanTextClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            if (mSpanTextClickListener != null) {
                mSpanTextClickListener.setSpanText(mDynamicPosition, mNote, mAmount, mTextView, canNotRead);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (mSpanTextColor != null) {
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
        if (mEndPos > temp.length()) {
            mEndPos = temp.length();
        }
        try {
            spanableInfo.setSpan(new SpanTextClickable(), mStartPos, mEndPos, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            spanableInfo.setSpan(new SpanTextClickable(), 0, temp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanableInfo;
    }

    public interface OnSpanTextClickListener {
        void setSpanText(int position, int note, int amount, TextView view, boolean canNotRead);
    }
}

