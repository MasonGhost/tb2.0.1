package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.R;

/**
 * @Describe 自定义带结束省略号的 TextViwe
 * @Author Jungle68
 * @Date 2017/12/21
 * @Contact master.jungle68@gmail.com
 */
public class SpanTextViewWithEllipsize extends android.support.v7.widget.AppCompatTextView {
    private SpannableStringBuilder mSpannableStringBuilder
            = new SpannableStringBuilder();
    private boolean mIsShowDot = true;
    private int mMaxlines = 3;

    public SpanTextViewWithEllipsize(Context context) {
        super(context);
    }

    public SpanTextViewWithEllipsize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpanTextViewWithEllipsize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 注意：spannableString 设置Spannable 的对象到spannableString中时，要用Spannable.SPAN_EXCLUSIVE_EXCLUSIVE的flag值，不然可能会会出现后面的衔接字符串不会显示
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsShowDot) {
            CharSequence charSequence = getText();
            int lastCharDown = 0;
            try {
                lastCharDown = getLayout().getLineVisibleEnd(mMaxlines - 1);
            } catch (Exception ignored) {
            }
            if (lastCharDown > 0 && charSequence.length() > lastCharDown) {
                mSpannableStringBuilder.clear();
                mSpannableStringBuilder.append(charSequence.subSequence(0, lastCharDown - 1)).append(getContext().getString(R.string
                        .string_ellipsis_dot));
                setText(mSpannableStringBuilder);
            }
        }
        super.onDraw(canvas);
    }

    public void setShowDot(boolean showDot, int maxlines) {
        mIsShowDot = showDot;
        mMaxlines = maxlines;
        postInvalidate();
    }

}
