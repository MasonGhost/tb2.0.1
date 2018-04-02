package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/21
 * @Contact master.jungle68@gmail.com
 */
public class SpanTextViewWithEllipsize extends android.support.v7.widget.AppCompatTextView {
    private SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

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
        CharSequence charSequence = getText();
        int lastCharDown = 0;
        try {
            // 最多显示 3 行
            lastCharDown = getLayout().getLineVisibleEnd(2);
        } catch (Exception ignored) {
        }
        if (lastCharDown > 0 && charSequence.length() > lastCharDown) {
            spannableStringBuilder.clear();
            spannableStringBuilder.append(charSequence.subSequence(0, lastCharDown - 1)).append("...");
            setText(spannableStringBuilder);
        }
        super.onDraw(canvas);
    }
}
