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

    private Integer mSpanTextColor;

    private Integer mCharNum;

    public TextViewUtils(TextView textView, String oriMsg) {
        mTextView = textView;
        mOriMsg = oriMsg;
        textView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
        textView.setText(part());
    }

    public TextViewUtils setPartCharNum(int charNum) {
        mCharNum = charNum;
        return this;
    }

    public TextViewUtils setSpanTextColor(int spanTextColor) {
        mSpanTextColor = spanTextColor;
        return this;
    }

    public TextViewUtils setOnSpanTextClickListener(OnSpanTextClickListener spanTextClickListener) {
        mSpanTextClickListener = spanTextClickListener;
        return this;
    }

    public TextViewUtils setPos(int startPos, int endPos) {
        mStartPos = startPos;
        mEndPos = endPos;
        return this;
    }


    class SpanTextClickable extends ClickableSpan implements View.OnClickListener {
        @Override
        public void onClick(View widget) {
            mSpanTextClickListener.setSpanText(mTextView, canNotRead);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (mSpanTextColor == null) {
                ds.setColor(Color.BLUE);
            } else {
                ds.setColor(ds.linkColor);
            }
            ds.setUnderlineText(false);    //去除超链接的下划线
            if (canNotRead) {
                BlurMaskFilter blurMaskFilter = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
                ds.setMaskFilter(blurMaskFilter);
            } else {
                ds.setMaskFilter(null);
            }
        }
    }

    private SpannableString part() {
        String temp = "";
        if (mCharNum != null) {
            temp = mOriMsg.substring(0, mCharNum) + "...展开";
        } else {
            temp = mOriMsg.substring(0, mOriMsg.length() / 2) + "...展开";
        }
        return getSpannableString(temp);
    }

    private SpannableString open() {
        String temp = mOriMsg + "收起";
        return getSpannableString(temp);
    }

    private SpannableString getSpannableString(CharSequence temp) {
        SpannableString spanableInfo = new SpannableString(temp);
        spanableInfo.setSpan(new SpanTextClickable(), mStartPos == 0 ? temp.length() - 2 : mStartPos, mEndPos == 0 ? temp.length() : mEndPos,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

    public interface OnSpanTextClickListener {
        void setSpanText(TextView view, boolean canNotRead);
    }
}
